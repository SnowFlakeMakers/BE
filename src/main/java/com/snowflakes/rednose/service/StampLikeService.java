package com.snowflakes.rednose.service;

import com.snowflakes.rednose.dto.like.stamp.ShowStampLikeResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampLike;
import com.snowflakes.rednose.exception.AlreadyExistException;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampLikeRepository;
import com.snowflakes.rednose.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.snowflakes.rednose.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.snowflakes.rednose.exception.ErrorCode.STAMP_LIKE;
import static com.snowflakes.rednose.exception.ErrorCode.STAMP_LIKE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampLikeService {

    private final StampLikeRepository stampLikeRepository;
    private final StampRepository stampRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void like(Long stampId, Long memberId) {
        Stamp stamp = findStampById(stampId);
        Member member= findMemberById(memberId);
        if (stampLikeRepository.existsByMemberIdAndStampId(memberId, stampId)) {
            throw new AlreadyExistException(STAMP_LIKE);
        }
        StampLike like = StampLike.builder().stamp(stamp).member(member).build();
        stampLikeRepository.save(like);
        stamp.like();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    private Stamp findStampById(Long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    public ShowStampLikeResponse getLikes(Long memberId) {
        Slice<Stamp> stamps = stampRepository.findLikesByMemberId(memberId);
        return ShowStampLikeResponse.from(stamps);
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
            throw new NotFoundException(MEMBER_NOT_FOUND);
        }
    }

    private StampLike findStampLikeByMemberIdAndStampId(Long memberId, Long stampId) {
        return stampLikeRepository.findByMemberIdAndStampId(memberId, stampId).orElseThrow(() -> new NotFoundException(STAMP_LIKE_NOT_FOUND));
    }
}
