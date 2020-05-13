package com.facevisitor.api.owner.controller;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.owner.service.ManageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/manage")
@AllArgsConstructor
public class OwnerController {

    ManageService manageService;

    @GetMapping("/user")
    public ResponseEntity userList(@RequestParam Long storeId){
        List<User> usersByStoreId = manageService.findUsersByStoreId(storeId);
        return ResponseEntity.ok(usersByStoreId);
    }



}
