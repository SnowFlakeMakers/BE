package com.snowflakes.rednose.dto.stampcraft;

import lombok.Builder;

public class CreateStampCraftResponse {

    private Long stampCraftId;

    public Long getStampCraftId() {
        return stampCraftId;
    }

    @Builder
    public CreateStampCraftResponse(Long stampCraftId) {
        this.stampCraftId = stampCraftId;
    }

    public static CreateStampCraftResponse from(Long stampCraftId) {
        return CreateStampCraftResponse.builder().stampCraftId(stampCraftId).build();
    }
}
