package com.snowflakes.rednose.dto.stampcraft;

import lombok.Builder;

public class EnterStampCraftResponse {

    private String nickname;

    @Builder
    public EnterStampCraftResponse(String nickname) {
        this.nickname = nickname;
    }

    public static EnterStampCraftResponse from(String nickname) {
        return EnterStampCraftResponse.builder().nickname(nickname).build();
    }
}
