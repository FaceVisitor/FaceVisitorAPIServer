package com.facevisitor.api.controller;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.service.user.UserService;
import java.security.Principal;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    ResponseEntity me(Principal principal){
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/exist")
    ResponseEntity checkExist(@RequestBody HashMap<String,String> payload){
        String email = payload.get("email");
        log.debug("email : {}" , email);
        return ResponseEntity.ok(userService.exist(email));
    }
}
