package com.facevisitor.api.owner.controller;

import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.dto.auth.OLogin;
import com.facevisitor.api.owner.service.OauthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/owner/auth")
@Slf4j
@AllArgsConstructor
public class OAuthController {

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


}
