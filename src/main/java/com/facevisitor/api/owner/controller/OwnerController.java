package com.facevisitor.api.owner.controller;

import com.facevisitor.api.owner.service.ManageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner/manage")
@AllArgsConstructor
public class OwnerController {

    ManageService manageService;

    @GetMapping("/user")
    public ResponseEntity userList(@RequestParam Long storeId) {
        return ResponseEntity.ok(manageService.findUsersByStoreId(storeId));
    }

    @GetMapping("/order")
    public ResponseEntity orderList(@RequestParam Long storeId) {
        System.out.println("store Id : " + storeId);
        return ResponseEntity.ok(manageService.findOrdersByStore(storeId));
    }


    @GetMapping("/user/order/{userId}")
    public ResponseEntity userOrderList(@RequestParam Long storeId, @PathVariable Long userId) {
        return ResponseEntity.ok(manageService.findOrdersByStoreAndUser(storeId, userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity userDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(manageService.getUserById(userId));
    }


}
