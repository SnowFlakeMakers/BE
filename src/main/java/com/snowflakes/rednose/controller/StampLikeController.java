package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.dto.like.stamp.ShowStampLikeResponse;
import com.snowflakes.rednose.service.StampLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stamps")
public class StampLikeController {

    private final StampLikeService stampLikeService;

    @PostMapping("/{stampId}")
    public ResponseEntity<Void> like(Long memberId, @PathVariable Long stampId) {
        stampLikeService.like(stampId, memberId);
        return ResponseEntity.created(URI.create("/api/v1/stamps")).build();
    }

    @GetMapping("/likes")
    public ShowStampLikeResponse show(Long memberId) {
        return stampLikeService.getLikes(memberId);
    }

}
