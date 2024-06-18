package com.xaaef.molly.auth.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.auth.service.RsaAsymmetricCryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.0
 * @date 2024/6/18 下午2:18
 */


@Slf4j
@Service
public class RsaAsymmetricCryptoServiceImpl implements RsaAsymmetricCryptoService {

    private final RSA _rsa;

    public RsaAsymmetricCryptoServiceImpl(ApplicationContext context, JwtTokenService tokenService) throws IOException {
        var pemPrivateKeyPath = tokenService.getProps().getRsaPrivateKey();
        var privateKeyContent = context.getResource(pemPrivateKeyPath).getContentAsString(StandardCharsets.UTF_8)
                .replaceFirst("-----BEGIN PRIVATE KEY-----", "")
                .replaceFirst("-----END PRIVATE KEY-----", "");

        var pemPublicKeyPath = tokenService.getProps().getRsaPublicKey();
        var publicKeyContent = context.getResource(pemPublicKeyPath).getContentAsString(StandardCharsets.UTF_8)
                .replaceFirst("-----BEGIN PUBLIC KEY-----", "")
                .replaceFirst("-----END PUBLIC KEY-----", "");

        _rsa = new RSA(privateKeyContent, publicKeyContent);
    }

    @Override
    public RSA getRSA() {
        return this._rsa;
    }

    @Override
    public String encrypt(String plainText) {
        return _rsa.encryptBase64(plainText, KeyType.PublicKey);
    }


    @Override
    public String decrypt(String cipherText) {
        return _rsa.decryptStr(cipherText, KeyType.PrivateKey);
    }


}
