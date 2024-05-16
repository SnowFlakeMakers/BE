package com.snowflakes.rednose.dto.stampcraft;

import com.snowflakes.rednose.entity.Member;
import lombok.Builder;

public class EnterStampCraftResponse {

    private String nickname;

    @Builder
    public EnterStampCraftResponse(String nickname) {
        this.nickname = nickname;
    }

    public static EnterStampCraftResponse from(Member member) {
        return EnterStampCraftResponse.builder()
                .nickname(member.getNickname())
                .build();
    }

    public String getNickname() {
        return nickname;
    }
}
