package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.MakeSealResponse;
import com.snowflakes.rednose.dto.seal.MakeSealRequest;
import com.snowflakes.rednose.dto.seal.ShowMySealsResponse;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.dto.seal.ShowSealSpecificResponse;
import com.snowflakes.rednose.dto.seal.ShowSealsResponse;
import com.snowflakes.rednose.service.PreSignedUrlService;
import com.snowflakes.rednose.service.SealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Slf4j

public class SealController {

    private final SealService sealService;
    private final PreSignedUrlService preSignedUrlService;

    @GetMapping("/seals/{sealId}")
    public ResponseEntity<ShowSealSpecificResponse> specific(@PathVariable Long sealId, @MemberId Long memberId) {
        ShowSealSpecificResponse showSealSpecificResponse = sealService.showSpecific(sealId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(showSealSpecificResponse);
    }

    @GetMapping("/my-seals")
    public ShowMySealsResponse showMySeals(Pageable pageable, @MemberId Long memberId) {
        return sealService.showMySeals(pageable, memberId);
    }

    @PostMapping("/seals")
    public MakeSealResponse make(@MemberId Long memberId, @RequestBody @Valid MakeSealRequest makeSealRequest) {
        return sealService.make(memberId, makeSealRequest);
    }

    @GetMapping("/seals")
    public ShowSealsResponse show(@RequestParam(required = false) String keyword, Pageable pageable) {
        return sealService.show(keyword, pageable);
    }

    @GetMapping("/seals/pre-signed-url")
    public CreatePreSignedUrlResponse getPreSignedUrl() {
      return preSignedUrlService.getSealPreSignedUrlForPut();
    }
}
