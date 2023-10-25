package com.xaaef.molly.redis;

import cn.hutool.core.text.StrPool;
import com.xaaef.molly.common.consts.ConfigName;
import com.xaaef.molly.common.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.xaaef.molly.common.consts.LoginConst.*;

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

    /**
     * 不隔离的多租户的 redis == equals
     */
    private static final List<String> IGNORE_EQUALS_KEYS = List.of(
            CAPTCHA_CODE_KEY,
            LOGIN_TOKEN_KEY,
            FORCED_OFFLINE_KEY,
            TenantUtils.X_TENANT_ID,
            ConfigName.REDIS_CACHE_KEY,
            ConfigName.USER_DEFAULT_PASSWORD,
            ConfigName.TENANT_DEFAULT_LOGO,
            ConfigName.TENANT_DEFAULT_ROLE_NAME,
            ConfigName.GET_HOLIDAY_URL
    );


    /**
     * 不隔离的多租户的 redis == contains
     */
    private static final List<String> IGNORE_CONTAINS_KEYS = List.of(
            "aaa",
            "bbb"
    );


    public static final StringRedisSerializer US_ASCII;
    public static final StringRedisSerializer ISO_8859_1;
    public static final StringRedisSerializer UTF_8;

    static {
        US_ASCII = new StringRedisSerializer(StandardCharsets.US_ASCII);
        ISO_8859_1 = new StringRedisSerializer(StandardCharsets.ISO_8859_1);
        UTF_8 = new StringRedisSerializer(StandardCharsets.UTF_8);
    }

    private static final String TENANT_PREFIX = "tid";

    private static final Boolean IS_LOG = false;

    private final Charset charset;

    public TenantStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }


    public TenantStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }


    @Override
    public byte[] serialize(String string) throws SerializationException {
        if (StringUtils.isBlank(string)) {
            echoLog(string);
            return null;
        }
        if (StringUtils.isBlank(TenantUtils.getTenantId())) {
            echoLog(string);
            return string.getBytes(charset);
        }
        // 本身带有多租户ID的不拼接
        if (string.indexOf(StrPool.COLON) > 0 && string.startsWith(TENANT_PREFIX)) {
            echoLog(string);
            return string.getBytes(charset);
        }
        // true：拼接多租户ID
        boolean flag = true;
        if (!IGNORE_EQUALS_KEYS.isEmpty()) {
            for (String key : IGNORE_EQUALS_KEYS) {
                if (key.equals(string)) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            if (!IGNORE_EQUALS_KEYS.isEmpty()) {
                for (String key : IGNORE_EQUALS_KEYS) {
                    if (string.startsWith(key)) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        if (flag) {
            echoLog(TenantUtils.getTenantId() + StrPool.COLON + string);
            return (TenantUtils.getTenantId() + StrPool.COLON + string).getBytes(charset);
        }
        echoLog(string);
        return string.getBytes(charset);
    }


    private void echoLog(String key) {
        if (IS_LOG) {
            log.info("echoLog redis key:  {} ", key);
        }
    }


    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset)
                .replaceFirst(TenantUtils.getTenantId() + ":", ""));
    }

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }

}
