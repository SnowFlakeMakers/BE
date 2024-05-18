package com.snowflakes.rednose.service;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.entity.SealLike;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.exception.errorcode.SealErrorCode;
import com.snowflakes.rednose.exception.errorcode.SealLikeErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.SealLikeRepository;
import com.snowflakes.rednose.repository.SealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SealLikeService {

    private final MemberRepository memberRepository;
    private final SealRepository sealRepository;
    private final SealLikeRepository sealLikeRepository;

    @Transactional
    public void like(Long sealId, Long memberId) {
        Member member = findMemberById(memberId);
        Seal seal = findSealById(sealId);
        validAlreadyLikeSeal(sealId, memberId);
        SealLike sealLike = SealLike.builder().member(member).seal(seal).build();
        sealLikeRepository.save(sealLike);
        seal.like();
    }

    private void validAlreadyLikeSeal(Long sealId, Long memberId) {
        if (sealLikeRepository.existsByMemberIdAndSealId(memberId, sealId)) {
            throw new BadRequestException(SealLikeErrorCode.ALREADY_EXIST);
        }
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }

    private Seal findSealById(Long sealId) {
        return sealRepository.findById(sealId)
                .orElseThrow(() -> new NotFoundException(SealErrorCode.NOT_FOUND));
    }
}
