package com.xaaef.molly.auth.service;

import java.awt.image.BufferedImage;

/**
 * <p>
 * 验证码
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0
 * @createTime 2020/3/5 0005 11:28
 */

public interface LineCaptchaService {

    /**
     * 随机获取图形验证码
     *
     * @param codeKey
     * @return ImageVerifyCode
     * @author Wang Chen Chen<932560435@qq.com>
     * @createTime 2020/3/5 0005 11:31
     */
    BufferedImage random(String codeKey);

    /**
     * 删除在 redis 中缓存的。图形验证码值
     *
     * @param codeKey
     * @return ImageVerifyCode
     * @author Wang Chen Chen<932560435@qq.com>
     * @createTime 2020/3/5 0005 11:31
     */
    void delete(String codeKey);

    /**
     * 删除在 redis 中缓存的。图形验证码值
     *
     * @param codeKey
     * @param userCodeText
     * @return ImageVerifyCode
     * @author Wang Chen Chen<932560435@qq.com>
     * @createTime 2020/3/5 0005 11:31
     */
    boolean check(String codeKey, String userCodeText);

}
