package com.book.event.infrastructure.repositories.redis.ExpirationHandling;

import com.book.event.transport.queue.producer.Producer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@AllArgsConstructor
@Configuration
public class ExpirationHandling {

    private Producer messageProducer;

    @Bean
    RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);

        listenerContainer.addMessageListener((message, pattern) -> {

            RedisSerializer<String> serializer = new StringRedisSerializer();

            messageProducer.sendMessage("Book_Rollback_Topic",
                    serializer.deserialize(message.getBody()));

        }, new PatternTopic("__keyevent@*__:expired"));

        return listenerContainer;
    }
}
