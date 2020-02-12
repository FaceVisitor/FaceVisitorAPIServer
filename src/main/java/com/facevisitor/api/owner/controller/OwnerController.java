package com.facevisitor.api.owner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/owner/me")
public class OwnerController {

    @GetMapping("/")
    public ResponseEntity me(Principal principal){
        return null;
    }
}
