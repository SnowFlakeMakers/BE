package com.snowflakes.rednose.dto.stampcraft;

import lombok.Builder;

public class LeaveStampCraftResponse {

    private String nickname;

    @Builder
    public LeaveStampCraftResponse(String nickname) {
        this.nickname = nickname;
    }

    public static LeaveStampCraftResponse from(String nickname) {
        return LeaveStampCraftResponse.builder().nickname(nickname).build();
    }
}
