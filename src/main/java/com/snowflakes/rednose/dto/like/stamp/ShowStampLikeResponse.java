package com.snowflakes.rednose.dto.like.stamp;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;
import org.springframework.data.domain.Slice;
import java.util.List;

public class ShowStampLikeResponse {

    private boolean hasNext;
    private List<StampResponse> contents;

    @Builder
    public ShowStampLikeResponse(boolean hasNext, List<StampResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }

    public static ShowStampLikeResponse from(Slice<Stamp> stamps) {
        return ShowStampLikeResponse.builder()
                .hasNext(stamps.hasNext())
                .contents(stamps.stream().map(StampResponse::of).toList())
                .build();
    }
}
