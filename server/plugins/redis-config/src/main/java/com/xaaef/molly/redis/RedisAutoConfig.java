package com.xaaef.molly.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


/**
 * <p>
 * redis 和 spring cache 配置
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 2.0
 * @date 2019/12/12 23:12
 */

@Slf4j
@Configuration
@AllArgsConstructor
@ConditionalOnClass({
        RedisConnectionFactory.class,
        StringRedisTemplate.class
})
@EnableConfigurationProperties(CacheProperties.class)
public class RedisAutoConfig {

    private final CacheProperties cacheProps;

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        var redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);

        var valueSerializer = valueSerializer();
        var keySerializer = keySerializer();

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        log.debug("自定义 RedisTemplate 加载完成");
        return redisTemplate;
    }


    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        var keySerializer = keySerializer();
        var valueSerializer = valueSerializer();
        var redisProps = cacheProps.getRedis();
        var entryTtl = redisProps.getTimeToLive() == null ? Duration.ofSeconds(180) : redisProps.getTimeToLive();
        var config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(entryTtl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .disableCachingNullValues();
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }


    private static RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }


    private static RedisSerializer<Object> valueSerializer() {
        return new Jackson2JsonRedisSerializer<>(Object.class);
    }


    /**
     * redis 缓存工具类
     *
     * @param stringRedisTemplate
     * @return RedisCacheUtils
     * @author WangChenChen
     * @date 2022/5/16 10:02
     */
    @Bean
    public RedisCacheUtils redisCacheUtils(StringRedisTemplate stringRedisTemplate) {
        return new RedisCacheUtils(stringRedisTemplate);
    }

}

