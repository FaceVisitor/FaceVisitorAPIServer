package com.facevisitor.api.controller;

import com.facevisitor.api.service.personalize.PersonalizeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/personalize")
@AllArgsConstructor
public class PersonalizeController {

    PersonalizeService personalizeService;

    @GetMapping("{userId}")
    public ResponseEntity getRecommend(@PathVariable Long userId) {
        return ResponseEntity.ok(personalizeService.getRecommendations(userId));
    }

    @GetMapping("/ranking/{userId}")
    public ResponseEntity getRanking(@PathVariable Long userId) {
        return ResponseEntity.ok(personalizeService.getRankings(userId));
    }

    @PostMapping("{userId}/event/view/{itemId}")
    public ResponseEntity viewEvent(@PathVariable Long userId, @PathVariable Long itemId) throws JsonProcessingException {
        personalizeService.viewEvent(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{userId}/event/like/{itemId}")
    public ResponseEntity likeEvent(@PathVariable Long userId, @PathVariable Long itemId) throws JsonProcessingException {
        personalizeService.likeEvent(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{userId}/event/dislike/{itemId}")
    public ResponseEntity dislikeEvent(@PathVariable Long userId, @PathVariable Long itemId) throws JsonProcessingException {
        personalizeService.disLikeEvent(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{userId}/event/cart/{itemId}")
    public ResponseEntity cartEvent(@PathVariable Long userId, @PathVariable Long itemId) throws JsonProcessingException {
        personalizeService.cartEvent(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{userId}/event/order/{itemId}")
    public ResponseEntity orderEvent(@PathVariable Long userId, @PathVariable Long itemId) throws JsonProcessingException {
        personalizeService.orderEvent(userId, itemId);
        return ResponseEntity.ok().build();
    }
}
