package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.MakeSealResponse;
import com.snowflakes.rednose.dto.seal.MakeSealRequest;
import com.snowflakes.rednose.dto.seal.ShowMySealsResponse;
import com.snowflakes.rednose.dto.seal.ShowSealSpecificResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.exception.errorcode.SealErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.SealLikeRepository;
import com.snowflakes.rednose.repository.SealRepository;
import java.time.LocalDateTime;
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
    private final SealLikeRepository sealLikeRepository;
    private final MemberRepository memberRepository;

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
        return ShowMySealsResponse.from(seals);
    }

    public MakeSealResponse make(Long memberId, MakeSealRequest makeSealRequest) {
        Member member = findMemberById(memberId);
        Seal seal = sealRepository.save(
                Seal.builder().createdAt(LocalDateTime.now()).member(member).imageUrl(makeSealRequest.getImage())
                        .numberOfLikes(0).build());
        return MakeSealResponse.builder().sealId(seal.getId()).build();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }
}