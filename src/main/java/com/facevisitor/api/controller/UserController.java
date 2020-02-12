package com.facevisitor.api.controller;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    final UserService userService;

    @Inject
    private TokenEndpoint tokenEndpoint;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    ResponseEntity me(Principal principal){
        User user = userService.getUserByEmail(principal.getName());
        log.debug(String.valueOf(user));
        return ResponseEntity.ok(user);
    }

//    ResponseEntity getToken(Principal principal) throws HttpRequestMethodNotSupportedException {
//        HashMap<String, String> parameters = new HashMap<String, String>();
//        parameters.put("client_id", "appid");
//        parameters.put("client_secret", "myOAuthSecret");
//        parameters.put("grant_type", "password");
//        parameters.put("password", myUser.getPassword());
//        parameters.put("scope", "read write");
//        parameters.put("username", myUser.getLogin());
//
//        return tokenEndpoint.getAccessToken(principal, parameters);
//    }

    @PostMapping("/exist")
    ResponseEntity checkExist(@RequestBody HashMap<String,String> payload){
        String email = payload.get("email");
        log.debug("email : {}" , email);
        return ResponseEntity.ok(userService.exist(email));
    }
}
