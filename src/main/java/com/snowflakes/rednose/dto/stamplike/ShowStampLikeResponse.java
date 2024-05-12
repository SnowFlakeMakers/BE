package com.snowflakes.rednose.dto.stamplike;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import java.util.List;

@Getter
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
                .contents(stamps.stream().map(StampResponse::from).toList())
                .build();
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public List<StampResponse> getContents() {
        return contents;
    }
}
