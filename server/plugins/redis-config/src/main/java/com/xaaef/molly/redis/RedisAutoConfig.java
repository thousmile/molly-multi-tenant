package com.xaaef.molly.redis;

import com.xaaef.molly.common.consts.RedisKeyConst;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.common.util.TenantUtils;
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
        redisTemplate.setKeySerializer(tenantKeySerializer());
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        var redisTemplate = new StringRedisTemplate();
        redisTemplate.setKeySerializer(tenantKeySerializer());
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        var tenantKeySerializer = tenantKeySerializer();
        var valueSerializer = valueSerializer();
        var redisProps = cacheProps.getRedis();
        var entryTtl = redisProps.getTimeToLive() == null ? Duration.ofSeconds(180) : redisProps.getTimeToLive();
        var config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(entryTtl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(tenantKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .disableCachingNullValues();
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }


    private static RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }


    private static RedisSerializer<String> tenantKeySerializer() {
        return new TenantStringRedisSerializer(
                RedisKeyConst.IGNORE_EQUALS_KEYS,
                RedisKeyConst.IGNORE_STARTS_WITH_KEYS,
                TenantUtils::getTenantId
        );
    }


    private static RedisSerializer<Object> valueSerializer() {
        return new Jackson2JsonRedisSerializer<>(JsonUtils.getMapper(), Object.class);
    }


}

