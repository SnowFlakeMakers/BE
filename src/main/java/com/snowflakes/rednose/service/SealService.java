package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.seal.SealResponse;
import com.snowflakes.rednose.dto.seal.ShowMySealsResponse;
import com.snowflakes.rednose.dto.stamp.CreatePreSignedUrlResponse;
import com.snowflakes.rednose.dto.seal.ShowSealSpecificResponse;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.SealErrorCode;
import com.snowflakes.rednose.repository.SealLikeRepository;
import com.snowflakes.rednose.repository.SealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SealService {
  
    private final SealRepository sealRepository;
    private final PreSignedUrlService preSignedUrlService;
    private final SealLikeRepository sealLikeRepository;

    public ShowSealSpecificResponse showSpecific(Long sealId, Long memberId) {
        Seal seal = findSealById(sealId);
        boolean liked = sealLikeRepository.existsByMemberIdAndSealId(memberId, sealId);
        return ShowSealSpecificResponse.of(seal, liked);
    }

    private Seal findSealById(Long sealId) {
        return sealRepository.findById(sealId).orElseThrow(() -> new NotFoundException(SealErrorCode.NOT_FOUND));
    }

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