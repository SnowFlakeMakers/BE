package com.snowflakes.rednose.service;


import com.snowflakes.rednose.dto.member.SignInRequest;
import com.snowflakes.rednose.entity.Member;
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
            throw new RuntimeException("닉네임 중복");
        }
        return memberRepository.save(Member.builder().socialId(request.getSocialId()).usable(true).build());
    }
}
