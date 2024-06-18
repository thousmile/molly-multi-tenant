package com.xaaef.molly.auth.service;

import cn.hutool.crypto.asymmetric.RSA;

/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.0
 * @date 2024/6/18 下午2:17
 */


public interface RsaAsymmetricCryptoService {

    /**
     * @date 2024/6/18 下午2:17
     */
    RSA getRSA();

    /**
     * 加密
     *
     * @date 2024/6/18 下午2:17
     */
    String encrypt(String plainText);


    /**
     * 解密
     *
     * @date 2024/6/18 下午2:17
     */
    String decrypt(String cipherText);

}
