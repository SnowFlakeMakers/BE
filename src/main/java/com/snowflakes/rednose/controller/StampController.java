package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlRequest;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.dto.stamp.ShowMyStampsRes
import com.snowflakes.rednose.dto.stamp.ShowStampsResponse;
import com.snowflakes.rednose.service.StampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StampController {

    private final StampService stampService;

    @GetMapping("/stamps")
    public ShowStampsResponse show(Pageable pageable) {
        return stampService.show(pageable);
    }

    @GetMapping("/my-stamps")
    public ShowMyStampsResponse showMyStamps(Pageable pageable, Long memberId) {
        return stampService.showMyStamps(pageable, 1L);
    }
}
