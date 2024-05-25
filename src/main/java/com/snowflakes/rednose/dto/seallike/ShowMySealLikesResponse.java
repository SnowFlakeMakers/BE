package com.snowflakes.rednose.dto.seallike;

import com.snowflakes.rednose.dto.seal.SealResponse;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class ShowMySealLikesResponse {

    private boolean hasNext;
    private List<SealResponse> contents;

    public ShowMySealLikesResponse(boolean hasNext, List<SealResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }
}
