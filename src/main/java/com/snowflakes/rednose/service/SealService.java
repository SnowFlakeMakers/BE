package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.seal.SealResponse;
import com.snowflakes.rednose.dto.seal.ShowMySealsResponse;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.repository.SealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class SealService {

    private final SealRepository sealRepository;
    private final PreSignedUrlService preSignedUrlService;

    public ShowMySealsResponse showMySeals(Pageable pageable, Long memberId) {
        Slice<Seal> seals = sealRepository.findAllByMemberIdOrderByCreatedAtAsc(memberId, pageable);
        return new ShowMySealsResponse(
                seals.hasNext(),
                seals.stream().map(s -> makeSealResponse(s)).toList()
        );
    }

    private SealResponse makeSealResponse(Seal seal) {
        String imageUrl = preSignedUrlService.getPreSignedUrlForShow(seal.getImageUrl());
        return SealResponse.of(seal, imageUrl);
    }

    public CreatePreSignedUrlResponse getPreSignedUrl() {
        return new CreatePreSignedUrlResponse(preSignedUrlService.getSealPreSignedUrlForPut());
    }
}
