package com.snowflakes.rednose.dto.seal;

import com.snowflakes.rednose.entity.Seal;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;

public class ShowMySealsResponse {

    private boolean hasNext;
    private List<SealResponse> contents;

    @Builder
    public ShowMySealsResponse(boolean hasNext, List<SealResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }

    public static ShowMySealsResponse from(Slice<Seal> seals) {
        return ShowMySealsResponse.builder()
                .hasNext(seals.hasNext())
                .contents(seals.stream().map(SealResponse::from).toList())
                .build();
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public List<SealResponse> getContents() {
        return contents;
    }

}
