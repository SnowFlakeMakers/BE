package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/members")
    public ResponseEntity<Void> cancelMembership(@MemberId Long memberId) {
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }

}
