package com.snowflakes.rednose.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ShowStampsResponse show() {
        return stampService.show();
    }
}
