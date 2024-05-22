package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.dto.seal.ShowMySealsResponse;
import com.snowflakes.rednose.service.SealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SealController {

    private final SealService sealService;

    @GetMapping("/my-seals")
    public ShowMySealsResponse showMySeals(Pageable pageable, Long memberId) {
        return sealService.showMySeals(pageable, memberId);
    }

}
