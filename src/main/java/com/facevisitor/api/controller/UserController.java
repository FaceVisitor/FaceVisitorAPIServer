package com.facevisitor.api.controller;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.UserDTO;
import com.facevisitor.api.service.goods.GoodsHistoryService;
import com.facevisitor.api.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@AllArgsConstructor
public class UserController {


    UserService userService;

    ModelMapper modelMapper;

    GoodsHistoryService goodsHistoryService;

    @PostMapping("/id")
    ResponseEntity getId(@RequestBody HashMap<String, String> payload) {
        String email = payload.get("email");
        return ResponseEntity.ok(userService.getId(email));
    }

    @GetMapping("/me")
    ResponseEntity me(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(modelMapper.map(user, UserDTO.MeResponseDTO.class));
    }

    @PostMapping("/exist")
    ResponseEntity checkExist(@RequestBody HashMap<String, String> payload) {
        String email = payload.get("email");
        log.debug("email : {}", email);
        return ResponseEntity.ok(userService.exist(email));
    }

    @GetMapping("/like/goods")
    ResponseEntity listGoods(Principal principal) {
        return ResponseEntity.ok(userService.listLikeGoods(principal.getName()));
    }

    @PostMapping("/like/goods/{id}")
    ResponseEntity likeGoods(Principal principal, @PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(userService.likeGoods(principal.getName(), id));
    }

    @GetMapping("/history/goods")
    ResponseEntity getGoodsHistory(Principal principal) {
        return ResponseEntity.ok(goodsHistoryService.getHistory(principal.getName()));
    }


}
