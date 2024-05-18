package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.seallike.ShowMySealLikesResponse;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @Transactional
    public void cancel(Long sealId, Long memberId) {
        validateMemberExist(memberId);
        Seal seal = findSealById(sealId);
        SealLike sealLike = findSealLikeById(sealId, memberId);
        sealLikeRepository.delete(sealLike);
        seal.cancelLike();
    }

    public ShowMySealLikesResponse show(Long memberId, Pageable pageable) {
        Slice<Seal> seals = sealRepository.findMyLikesByMemberId(memberId, pageable);
        return ShowMySealLikesResponse.from(seals);
    }

    private void validateMemberExist(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(MemberErrorCode.NOT_FOUND);
        }
    }

    private SealLike findSealLikeById(Long sealId, Long memberId) {
        return sealLikeRepository.findByMemberIdAndSealId(memberId, sealId)
                .orElseThrow(() -> new NotFoundException(SealLikeErrorCode.NOT_FOUND));
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
