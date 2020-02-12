package com.facevisitor.api.dto.auth;

import lombok.Data;

@Data
public class TokenDto {
    String access_token;
    String refresh_token;
    String scope;
    String jti;
    int expires_in;
    String token_type;
}
