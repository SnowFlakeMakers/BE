package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.dto.seallike.ShowMySealLikesResponse;
import com.snowflakes.rednose.service.SealLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RequestMapping("/api/v1/seals")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SealLikeController {

    private final SealLikeService sealLikeService;

    @PostMapping("/{sealId}")
    public ResponseEntity<Void> like(Long memberId, @PathVariable Long sealId) {
        sealLikeService.like(sealId, memberId);
        return ResponseEntity.created(URI.create("/api/v1/seals")).build();
    }

    @DeleteMapping("/{sealId}")
    public ResponseEntity<Void> cancel(Long memberId, @PathVariable Long sealId) {
        sealLikeService.cancel(sealId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/likes")
    public ShowMySealLikesResponse show(Long memberId, Pageable pageable) {
        return sealLikeService.show(memberId, pageable);
    }
}
