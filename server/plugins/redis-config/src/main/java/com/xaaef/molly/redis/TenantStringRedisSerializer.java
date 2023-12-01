package com.xaaef.molly.redis;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Set;
import java.util.function.Supplier;

/**
 * <p>
 * redis 自定义序列化，用于保存租户号，隔离数据
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 2.0
 * @date 2019/12/12 23:12
 */

@Slf4j
public class TenantStringRedisSerializer implements RedisSerializer<String> {

    private final Supplier<String> getTenantId;

    /**
     * 不隔离的多租户的key。 equals
     * 如果集合中包含 "captcha_codes" 那么
     * 除了 captcha_codes 其他的 任何key 都需要拼接 租户ID
     */
    private final Set<String> ignoreEqualsKeys;

    /**
     * 不隔离的多租户的key。 startsWith
     * 如果集合中包含 "captcha_codes:" 那么
     * <p>
     * 以下的redis key 不拼接 租户ID：
     * key1 :  captcha_codes:123456
     * key2 :  captcha_codes:user:123456
     * <p>
     * 以下的 redis key 会在添加前缀 租户ID：
     * key1 :  hello:123456
     * key2 :  app:webui:123456
     */
    private final Set<String> ignoreStartsWithKeys;


    public TenantStringRedisSerializer(Set<String> ignoreEqualsKeys,
                                       Set<String> ignoreStartsWithKeys,
                                       Supplier<String> getTenantId) {
        this.ignoreEqualsKeys = ignoreEqualsKeys;
        this.ignoreStartsWithKeys = ignoreStartsWithKeys;
        this.getTenantId = getTenantId;
    }


    @Override
    public byte[] serialize(String str) throws SerializationException {
        if (StrUtil.isBlank(str)) {
            return new byte[0];
        }
        var tenantId = this.getTenantId.get();
        // 租户ID 为空，不拼接
        if (StrUtil.isNotBlank(tenantId)) {
            var prefix = tenantId + StrPool.COLON;
            // 如果不包含。就需要拼接租户Id
            if (!str.startsWith(prefix)) {
                // true：拼接多租户ID
                var flag = true;
                // 判断 ignoreEqualsKeys 是否包含 此key
                if (!ignoreEqualsKeys.isEmpty()) {
                    flag = ignoreEqualsKeys.stream().noneMatch(str::equals);
                }
                if (flag && !ignoreStartsWithKeys.isEmpty()) {
                    // 判断 ignoreStartsWithKeys 中 此key
                    flag = ignoreStartsWithKeys.stream().noneMatch(str::startsWith);
                }
                if (flag) {
                    return (prefix + str).getBytes();
                }
            }
        }
        return str.getBytes();
    }


    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        var str = new String(bytes);
        var tenantId = this.getTenantId.get();
        if (StrUtil.isNotBlank(str) && StrUtil.isNotBlank(tenantId)) {
            str = StrUtil.removePrefix(str, tenantId + StrPool.COLON);
        }
        return str;
    }


    @Override
    public Class<?> getTargetType() {
        return String.class;
    }


}
