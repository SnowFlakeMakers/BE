package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.stamp.ShowStampSpecificResponse;
import com.snowflakes.rednose.dto.stamp.ShowStampsResponse;
import com.snowflakes.rednose.service.StampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StampController {

    private final StampService stampService;

    @GetMapping("/stamps")
    public ResponseEntity<ShowStampsResponse> show(Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(stampService.show(pageable));
    }

    @GetMapping("/stamps/{stampId}")
    public ResponseEntity<ShowStampSpecificResponse> specific(@PathVariable Long stampId, @MemberId Long memberId) {

        ShowStampSpecificResponse showStampSpecificResponse = stampService.showSpecific(stampId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(showStampSpecificResponse);
    }
}
