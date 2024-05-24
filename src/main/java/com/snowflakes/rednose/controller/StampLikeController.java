package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.dto.stamplike.ShowStampLikeResponse;
import com.snowflakes.rednose.service.StampLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StampLikeController {

    private final StampLikeService stampLikeService;

    @PostMapping("/stamp-likes/{stampId}")
    public ResponseEntity<Void> like(Long memberId, @PathVariable Long stampId) {
        stampLikeService.like(stampId, memberId);
        return ResponseEntity.created(URI.create("/api/v1/stamps")).build();
    }

    @GetMapping("/stamp-likes")
    public ShowStampLikeResponse show(Long memberId, Pageable pageable) {
        return stampLikeService.getLikes(memberId, pageable);
    }

    @DeleteMapping("/stamp-likes/{stampId}")
    public ResponseEntity<Void> cancel(Long memberId, @PathVariable Long stampId) {
        stampLikeService.cancel(stampId, memberId);
        return ResponseEntity.ok().build();
    }

}
