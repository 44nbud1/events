package com.book.event.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import static com.mysql.cj.conf.PropertyKey.logger;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        // will use default value from rsc
        // host : localhost
        // port : 6379
        return new JedisConnectionFactory(new RedisStandaloneConfiguration());

    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);

        template.setValueSerializer(new GenericToStringSerializer(Object.class));
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + "localhost" + ":" + 6379)
                //    .setPassword(redisPassword)
                .setConnectTimeout(5000)
                .setConnectionMinimumIdleSize(5)
                .setConnectionPoolSize(10);

        return Redisson.create(config);
    }

//    @Bean
//    RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory, ExpirationListener expirationListener) {
//        Logger logger = LoggerFactory.getLogger(ExpirationListener.class);
//
//        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
//        listenerContainer.setConnectionFactory(connectionFactory);
//        listenerContainer.addMessageListener(expirationListener, new PatternTopic("__keyevent@*__:expired"));
//        listenerContainer.setErrorHandler(e -> logger.error("There was an error in redis key expiration listener container", e));
//
//        return listenerContainer;
//    }
//
//    @Component
//    public static class ExpirationListener implements MessageListener {
//        private static final Logger logger = LoggerFactory.getLogger(ExpirationListener.class);
//
//        @Override
//        public void onMessage(Message message, byte[] bytes) {
//            String key = message.toString();
//
////            String id = extractId(key);
//            String ddkey = new String(bytes);
//            String ddkedy = new String(message.getChannel());
//            logger.debug("expired key: {}", key);
//
//
//            System.out.println("TERHIT 2: "+ key);
//            System.out.println("TERHIT 2: "+ ddkedy);
//        }
//
////        private String extractId(String key){
////            return key.substring((keyPrefix + ":").length());
////        }
//    }

}