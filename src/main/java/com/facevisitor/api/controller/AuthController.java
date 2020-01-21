package com.facevisitor.api.controller;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.dto.user.Login;
import com.facevisitor.api.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid  Login.Request loginRequest, Errors errors) {

        log.debug("face Ids : {}", loginRequest.getFaceId());
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        List<String> faceIds = loginRequest.getFaceId();
        if(!(faceIds.size() >0)){
            throw new NotFoundException();
        }

        Map<String, String> login = authService.login(faceIds);
        String access_token = login.get("access_token");
        String refresh_token = login.get("refresh_token");
        String createdAt = login.get("createdAt");
        Login.Response response = new Login.Response(access_token,refresh_token,createdAt);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/join")
    public ResponseEntity<?> join(@RequestBody Join join) {
        log.debug("회원가입 요청  이름 : " + join.getName() + " 이메일 :" + join.getEmail() + " face ids" + join.getFaceIds());
        authService.join(join);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/refresh_token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity refreshToken(HttpServletRequest request, @RequestBody Map<String, String> payload) {
        if (StringUtils.isEmpty(payload)) {
            throw new BadRequestException();
        }
        String refreshToken = payload.get("refresh_token");
        return authService.refreshToken(refreshToken, request);
    }

}
