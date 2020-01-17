package com.facevisitor.api.controller;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.dto.user.Login;
import com.facevisitor.api.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(HttpServletRequest request, @Valid @RequestBody Login login, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return authService.login(login, request);
    }

    @PostMapping(value = "/join")
    public ResponseEntity<?> join(@RequestBody Join join) {
        log.debug("회원가입 요청  이름 : " + join.getName() + " 이메일 :" + join.getEmail() + "face ids" + join.getFaceIds());
        return ResponseEntity.ok(authService.join(join));
    }

    @PostMapping(value = "/refresh_token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity refreshToken(HttpServletRequest request, @RequestBody Map<String, String> payload) {
        if (StringUtils.isEmpty(payload)) {
            throw new BadRequestException();
        }
        String refreshToken = payload.get("refresh_token");
        return authService.refreshToken(refreshToken, request);
    }

    @GetMapping("/me")
    public ResponseEntity me(@AuthenticationPrincipal User user) {
        if (user == null) {

        }
        log.debug("user : {}", user);
        return ResponseEntity.badRequest().build();
    }

}
