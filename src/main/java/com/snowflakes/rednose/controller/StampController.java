package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.stamp.ShowMyStampsResponse;
import com.snowflakes.rednose.dto.stamp.ShowStampSpecificResponse;
import com.snowflakes.rednose.dto.stamp.ShowStampsResponse;
import com.snowflakes.rednose.service.StampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StampController {

    private final StampService stampService;

    @GetMapping("/stamps")
    public ShowStampsResponse show(@RequestParam(required = false) String keyword, Pageable pageable) {
        return stampService.show(keyword, pageable);
    }

    @GetMapping("/stamps/{stampId}")
    public ShowStampSpecificResponse specific(@PathVariable Long stampId, @MemberId Long memberId) {
        return stampService.showSpecific(stampId, memberId);
    }

    @GetMapping("/my-stamps")
    public ShowMyStampsResponse showMyStamps(Pageable pageable,@MemberId Long memberId) {
        return stampService.showMyStamps(pageable, memberId);
    }
}
