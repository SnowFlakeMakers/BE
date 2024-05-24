package com.snowflakes.rednose.dto.stamp;

import com.snowflakes.rednose.entity.Stamp;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import java.util.List;

@Getter
public class ShowMyStampsResponse {

    private boolean hasNext;
    private List<StampResponse> contents;


    @Builder
    public ShowMyStampsResponse(boolean hasNext, List<StampResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }

    public static ShowMyStampsResponse from(Slice<Stamp> stamps) {
        return ShowMyStampsResponse.builder()
                .hasNext(stamps.hasNext())
                .contents(stamps.stream().map(StampResponse::from).toList())
                .build();
    }

}
