package com.example.demo.common.config;

import com.example.demo.common.service.Impl.RedisServiceImpl;
import com.example.demo.common.service.RedisService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
public class BaseRedisConfig extends CachingConfigurerSupport {
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                RedisErrorException(exception, key);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                RedisErrorException(exception, key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                RedisErrorException(exception, key);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                RedisErrorException(exception, null);
            }
        };
        return cacheErrorHandler;
    }

    protected void RedisErrorException(Exception exception,Object key){
        log.error("redis?????????key=[{}], exception={}", key, exception.getMessage());
    }


    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        // ?????? Jackson ?????????????????????
        RedisSerializer<Object> serializer=redisSerializer();

        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key ??? value
        // ??????????????? redis-key???????????????????????????
        // redis-value??????json???????????? json?????????????????????????????????????????????serializer?????????
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
    //@Bean
    public RedisSerializer<Object> redisSerializer(){
        //??????JSON????????????
        Jackson2JsonRedisSerializer<Object> serializer=new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //??????????????????????????????JSON??????????????????????????????Map??????
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheWriter redisCacheWriter=RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //??????Redis??????????????????1???
        RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
                .entryTtl(Duration.ofDays(1))
                .computePrefixWith(cacheName -> "caching:"+cacheName);

        // ??????????????????cacheName??????????????????????????????
//        Map<String, RedisCacheConfiguration> initialCacheConfiguration = new HashMap<String, RedisCacheConfiguration>() {{
//            put("demoCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMillis(20*1000)));
//            // ...
//        }};

        return new RedisCacheManager(redisCacheWriter,redisCacheConfiguration);
    }

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }
}
