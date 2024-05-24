package com.snowflakes.rednose.dto.stamplike;

import com.snowflakes.rednose.dto.stamp.StampResponse;
import lombok.Getter;
import java.util.List;

@Getter
public class ShowStampLikeResponse {

    private boolean hasNext;
    private List<StampResponse> contents;

    public ShowStampLikeResponse(boolean hasNext, List<StampResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }

}
