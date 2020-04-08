package com.facevisitor.api.controller;

import com.facevisitor.api.service.personalize.PersonalizeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
