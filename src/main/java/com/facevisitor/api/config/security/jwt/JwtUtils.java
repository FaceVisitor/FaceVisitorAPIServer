package com.facevisitor.api.config.security.jwt;

import com.facevisitor.api.common.exception.UnAuthorizedException;
import com.facevisitor.api.domain.user.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {

    @Value("${secret.oauth.clientId}")
    private String clientID; // 공개키

    @Autowired
    private KeyPair keyPair;

    public String createAccessToken(String email) {


        // 토큰 만료일 3일 후
        Date expireDate = new Date(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setExpiration(expireDate)
                .setId(UUID.randomUUID().toString()) // 고유값
                .claim("client_id", clientID)
                .claim("user_name", email)
                .claim("scope", Collections.singletonList("read"))
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }


    public String createRefreshToken(String email) {

        // 토큰 만료일 30일이 후
        Date expireDate = new Date(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        String compact = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setExpiration(expireDate)
                .setId(UUID.randomUUID().toString()) // 고유값
                .claim("client_id", clientID)
                .claim("user_name", email)
                .claim("scope", Collections.singletonList("read"))
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
        return compact;
    }

    public String getEmailFromToken(String token) {
        try {
            return tokenToClaims(token).getSubject();
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException("Expired");
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(" Some other exception in JWT parsing ");
        }
        return null;
    }

    public Boolean canRefresh(String token, int rangeMinutes) {
        if (tokenToClaims(token) != null) {
            long remainTime = tokenToClaims(token).getExpiration().getTime();
            long rangeTime = Date.from(Instant.now().plusSeconds(60 * rangeMinutes)).getTime();
            return remainTime < rangeTime;
        }
        return null;
    }

    public Claims tokenToClaims(String tokenValue) {
        try {
            return Jwts.parser().setSigningKey(keyPair.getPrivate()).parseClaimsJws(tokenValue).getBody();
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException("Expired");
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(" Some other exception in JWT parsing ");
        }
        return null;
    }
}
