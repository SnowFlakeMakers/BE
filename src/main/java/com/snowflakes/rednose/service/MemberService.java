package com.snowflakes.rednose.service;


import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.dto.member.SignInRequest;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member signIn(SignInRequest request) {
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new BadRequestException(MemberErrorCode.DUPLICATED_NICKNAME);
        }
        return memberRepository.save(Member.builder().socialId(request.getSocialId()).usable(true).build());
    }

    public Member getExistMember(UserInfo userinfo) {
        return memberRepository.findBySocialId(userinfo.getId())
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }
}
