package com.book.event.service.book.impl;

import com.book.event.config.RedisLock;
import com.book.event.entity.sql.event.Event;
import com.book.event.entity.sql.event.EventGroup;
import com.book.event.entity.sql.event.EventSchedule;
import com.book.event.enums.ScheduleEnum;
import com.book.event.exception.EventException;
import com.book.event.exception.InternalTimeoutException;
import com.book.event.infrastructure.repositories.redis.RedisRepository;
import com.book.event.infrastructure.repositories.sql.event.EventRepository;
import com.book.event.infrastructure.repositories.sql.event.EventScheduleRepository;
import com.book.event.service.book.BookService;
import com.book.event.service.event.ElasticService;
import com.book.event.service.model.BookOrder;
import com.book.event.service.model.BookRequest;
import com.book.event.service.model.BookResponse;
import com.book.event.service.model.GroupKey;
import com.book.event.transport.queue.producer.Producer;
import com.book.event.utils.IdGenerator;
import com.book.event.utils.Strings;
import com.book.event.utils.Time;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.book.event.validation.errorcode.EventErrorDescCode.*;

@Service
@AllArgsConstructor
@Slf4j
@EnableRetry(proxyTargetClass = true)
public class BookServiceImpl implements BookService {

    private static final String BookRedisKey = "Book_Created";

    private static final String BookKafkaTopic = "Book_Created_Topic";

    private RedisRepository redisRepository;

    private EventRepository eventRepository;

    private Producer messageProducer;

    private ElasticService elasticService;

    private EventScheduleRepository eventScheduleRepository;

    private final RedisLock redisLock;

    @PostConstruct
    public void init() {
        redisLock.init("Create-book-lock");
    }

    @Retryable(
            retryFor = InternalTimeoutException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000)
    )
    public BookResponse createBook(BookRequest bookRequest) {

        //  check the event if exist or not
        Event event = eventRepository.findByKey(bookRequest.getKey());

        if (event == null) {
            throw new EventException("event not found", DATA_NOT_FOUND);
        }

        // check
        Set<EventGroup> eventGroupSet = event.getEventGroupSet();

        List<GroupKey> groupKeys = new ArrayList<>();
        List<String> eventGroups = new ArrayList<>();

        // populate data from request and db
        bookRequest.getGroupKeys().forEach(k -> eventGroupSet.stream().toList().forEach(g -> {

            if (k.getKey().equals(g.getKey())) {

                groupKeys.add(GroupKey.builder()
                            .qty(k.getQty())
                            .price(g.getPrice())
                            .key(k.getKey())
                            .id(g.getId())
                            .redisKey(bookRequest.getKey() + g.getKey())
                        .build());

                eventGroups.add(k.getKey());
            }

        }));

        // check if request and db not
        if (bookRequest.getGroupKeys().size() != eventGroups.size()) {
            throw new EventException("product not found", DATA_NOT_FOUND);
        }

        AtomicReference<String> bookId = new AtomicReference<>("");
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);

        // check schedule
        EventSchedule eventSchedule = eventScheduleRepository.findByEvent(event);

        if (eventSchedule == null) {
            throw new EventException("Schedule not found", DATA_NOT_FOUND);
        }

        ScheduleEnum scheduleEnum = ScheduleEnum.getSchedule(eventSchedule.getLimitRatioType());

        redisRepository.inquiryQty("temp-qty-event");
        Object isTempQtyObj = redisRepository.inquiryQty("temp-qty-event");

        boolean isTempQtyActive;
        if (isTempQtyObj instanceof String) {
            isTempQtyActive = Boolean.parseBoolean((String) isTempQtyObj);
        } else {
            isTempQtyActive = false;
        }

        AtomicBoolean isTempQty = new AtomicBoolean(false);
        if (Time.inRange(new Date(), scheduleEnum.getStart(), scheduleEnum.getEnd())) {
            redisRepository.createQty("temp-qty-event", true);
            isTempQty.set(true);
        } else {
            isTempQty.set(false);
        }

        if (redisLock.tryLock()) {
            try {
                log.info("Lock");

                bookRequest.getGroupKeys().forEach(a -> {

                    String redisKey = bookRequest.getKey() + a.getKey();

                    bookId.set(IdGenerator.IdGen());
                    total.set(getTotalPrice(groupKeys));

                    // key of qty = Key + groupKey
                    Object Qty = redisRepository.inquiryQty(redisKey);

                    Integer qtyIn = null;
                    if (Qty instanceof String) {
                        qtyIn = Integer.valueOf((String) Qty);
                    }

                    if (qtyIn != null && qtyIn <= 0) {
                        throw new EventException("Invalid qty", PARAMETER_ERROR);
                    }

                    if (qtyIn == null || !isTempQty.get() && isTempQtyActive) {

                        redisRepository.createQty("temp-qty-event", false);

                        com.book.event.entity.elastic.event.Event e = elasticService.inquiryByKey(bookRequest.getKey());

                        if (e == null) {
                            throw new EventException("Data not found", DATA_NOT_FOUND);
                        }

                        e.getEventGroups().forEach(eventGroup -> {

                            if (a.getKey().equals(eventGroup.getKey())) {

                                if (eventGroup.getQty() > 0 && eventGroup.getQty() > a.getQty()) {
                                    redisRepository.createQty(redisKey, eventGroup.getQty() - a.getQty());
                                } else {
                                    throw new EventException("Qty sufficient", QTY_SUFFICIENT);
                                }
                            }
                        });
                    } else if (qtyIn < a.getQty()){
                            throw new EventException("Qty sufficient", QTY_SUFFICIENT);
                    } else {

                        // will override
                        if (qtyIn > scheduleEnum.getMax() && isTempQty.get() && !isTempQtyActive) {
                            redisRepository.createQty(redisKey, scheduleEnum.getMax());

                            isTempQty.set(false);
                        }

                        redisRepository.decrementQty(redisKey, a.getQty());
                    }
                });
            } catch (EventException e) {
                throw new InternalTimeoutException("Lock acquired by other thread", LOCK_TIMEOUT);
            } finally {

                log.info("Lock Released");

                redisLock.unlock();
            }
        } else {
            log.warn("Acquired by other thread");

            throw new InternalTimeoutException("Lock acquired by other thread", LOCK_TIMEOUT);
        }

        try {
            BookOrder bookOrder = BookOrder.builder()
                    .groupKeys(groupKeys)
                    .Key(String.valueOf(bookId))
                    .totalPrice(total.get())
                    .userId(bookRequest.getUserId())
                    .build();

            // save to the redis
            redisRepository.createEventWithTTL(
                    Strings.ObjectToJsonString(bookOrder),
                    BookRedisKey + bookRequest.getUserId() + bookId, 600000L
            );

            messageProducer.sendMessage(BookKafkaTopic, Strings.ObjectToJsonString(bookOrder));

        } catch (Exception e) {
            log.error("Error e: ",e);
            throw new InternalTimeoutException("Illegal parse", LOCK_TIMEOUT);
        }

        return BookResponse.builder()
                .bookId(bookId.get())
                .currency("IDR")
                .price(total.get())
                .build();
    }

    private BigDecimal getTotalPrice(List<GroupKey> bookOrder) {

        AtomicLong totalPrice = new AtomicLong(0L);
        bookOrder.forEach(b -> {
            totalPrice.addAndGet(b.totalPrice().longValue());
        });

        return new BigDecimal(String.valueOf(totalPrice));
    }

    @Recover
    public BookResponse recoverSaveTodo(InternalTimeoutException i, BookRequest bookRequest){

        log.warn("Recover req, {}", bookRequest);
        return BookResponse.builder().build();
    }

}
