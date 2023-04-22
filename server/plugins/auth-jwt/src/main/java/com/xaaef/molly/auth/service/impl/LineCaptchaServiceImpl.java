package com.xaaef.molly.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import com.xaaef.molly.auth.jwt.JwtTokenProperties;
import com.xaaef.molly.auth.service.LineCaptchaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static com.xaaef.molly.common.consts.LoginConst.CAPTCHA_CODE_KEY;


/**
 * <p>
 * 验证码
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.1
 * @createTime 2020/3/5 0005 11:32
 */


@Slf4j
@Service
@AllArgsConstructor
public class LineCaptchaServiceImpl implements LineCaptchaService {

    private final StringRedisTemplate redisTemplate;

    private final JwtTokenProperties props;

    private final static RandomGenerator RANDOM_GENERATOR = new RandomGenerator("13456789abcdefghjkmnpqrstuvwxyABCDEFGHJKMNPQRSTUVWXY", 4);

    @Override
    public BufferedImage random(String codeKey) {
        var lineCaptcha = CaptchaUtil.createCircleCaptcha(120, 50, 4, 20);
        lineCaptcha.setGenerator(RANDOM_GENERATOR);
        // 重新生成code
        lineCaptcha.createCode();
        // 将验证码的 codeKey 和 codeText , 保存在 redis 中，有效时间为 10 分钟
        redisTemplate.opsForValue().set(
                CAPTCHA_CODE_KEY + codeKey,
                lineCaptcha.getCode(),
                Duration.ofSeconds(props.getCaptchaExpired())
        );
        return lineCaptcha.getImage();
    }


    @Override
    public void delete(String codeKey) {
        redisTemplate.delete(CAPTCHA_CODE_KEY + codeKey);
    }


    @Override
    public boolean check(String codeKey, String userCodeText) {
        // 获取服务器的 CodeText
        String serverCodeText = redisTemplate.opsForValue().get(CAPTCHA_CODE_KEY + codeKey);
        // 将 serverCodeText 和 user.codeText 都转换成小写，然后比较
        return StringUtils.equalsIgnoreCase(serverCodeText, userCodeText);
    }

}
