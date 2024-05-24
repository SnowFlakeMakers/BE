package com.snowflakes.rednose.dto.seal;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class ShowMySealsResponse {

    private boolean hasNext;
    private List<SealResponse> contents;

    public ShowMySealsResponse(boolean hasNext, List<SealResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }

}
