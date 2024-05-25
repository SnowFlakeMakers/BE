package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.stamp.StampResponse;
import com.snowflakes.rednose.dto.stamplike.ShowStampLikeResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampLike;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.exception.errorcode.StampErrorCode;
import com.snowflakes.rednose.exception.errorcode.StampLikeErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampLikeRepository;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampLikeService {

    private final StampLikeRepository stampLikeRepository;
    private final StampRepository stampRepository;
    private final MemberRepository memberRepository;
    private final PreSignedUrlService preSignedUrlService;

    @Transactional
    public void like(Long stampId, Long memberId) {
        Stamp stamp = findStampById(stampId);
        Member member= findMemberById(memberId);
        validAlreadyLikedStamp(stampId, memberId);
        StampLike like = StampLike.builder().stamp(stamp).member(member).build();
        stampLikeRepository.save(like);
        stamp.like();
    }

    private void validAlreadyLikedStamp(Long stampId, Long memberId) {
        if (stampLikeRepository.existsByMemberIdAndStampId(memberId, stampId)) {
            throw new BadRequestException(StampLikeErrorCode.ALREADY_EXIST);
        }
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }

    private Stamp findStampById(Long stampId) {
        return stampRepository.findById(stampId)
                .orElseThrow(() -> new NotFoundException(StampErrorCode.NOT_FOUND));
    }

    public ShowStampLikeResponse getLikes(Long memberId, Pageable pageable) {
        Slice<Stamp> stamps = stampRepository.findLikesByMemberId(memberId, pageable);
        return new ShowStampLikeResponse(
                stamps.hasNext(),
                stamps.stream().map(s -> makeStampResponse(s)).toList()
        );
    }

    private StampResponse makeStampResponse(Stamp stamp) {
        String imageUrl = preSignedUrlService.getPreSignedUrlForShow(stamp.getImageUrl());
        return StampResponse.of(stamp, imageUrl);
    }

    @Transactional
    public void cancel(Long stampId, Long memberId) {
        validateMemberExist(memberId);
        Stamp stamp = findStampById(stampId);
        StampLike stampLike = findStampLikeByMemberIdAndStampId(memberId, stampId);
        stampLikeRepository.delete(stampLike);
        stamp.cancelLike();
    }

    private void validateMemberExist(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(MemberErrorCode.NOT_FOUND);
        }
    }

    private StampLike findStampLikeByMemberIdAndStampId(Long memberId, Long stampId) {
        return stampLikeRepository.findByMemberIdAndStampId(memberId, stampId)
                .orElseThrow(() -> new NotFoundException(StampLikeErrorCode.NOT_FOUND));
    }
}
