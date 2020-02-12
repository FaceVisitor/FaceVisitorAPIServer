package com.facevisitor.api.owner.controller;

import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.dto.auth.OLogin;
import com.facevisitor.api.owner.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/owner/auth")
@Slf4j
public class OAuthController {

    @Autowired
    OauthService authService;

    @PostMapping("/join")
    ResponseEntity oJoin(@RequestBody  OJoin join){
        authService.join(join);
        return ResponseEntity.ok(join);
    }

    @PostMapping("/login")
    public ResponseEntity oLogin(@RequestBody @Valid OLogin login, Errors errors) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(authService.login(login));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity oRefreshToken(@RequestBody  HashMap<String,String> payload) {
        String refreshToken = payload.get("refresh_token");
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

}
