package com.facevisitor.api.config.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
public class JwtConfig {

    /**
     * 키툴 생성 :
     * keytool -genkeypair -keyalg RSA -alias jwtkey-facevisitor -dname "cn=authority server, o=example, st=Seoul, c=KR" -keypass drq_RQgATmrKy%_w -keystore ./keystore.jks -storepass drq_RQgATmrKy%_w -storetype pkcs12
     * <p>
     * PASSWORD : drq_RQgATmrKy%_w
     * <p>
     * 인증서 생성
     * keytool -export -keystore keystore.jks -alias jwtkey-facevisitor -file jwtkey-facevisitor.cer
     * <p>
     * <p>
     * [리소스 서버에서 사용될 public key 생성 ]
     * openssl x509 -inform der -in jwtkey-facevisitor.cer -pubkey -noout
     */


    @Value("${jwt.keystore.alias}")
    private String keyStoreAlias;

    @Value("${jwt.keystore.password}")
    private String keyStorePassword;

    @Value("${jwt.keystore.publicKey}")
    private String publicKey;


    //For OauthServerConfig.java TokenStore

    @Bean
    public KeyPair keyPair() {
        return new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), keyStorePassword.toCharArray())
                .getKeyPair(keyStoreAlias);
    }

    //실제 사용
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Bean
    public JwtTokenStore customJwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


}
