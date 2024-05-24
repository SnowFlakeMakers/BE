package com.snowflakes.rednose.dto.seallike;

import com.snowflakes.rednose.dto.seal.SealResponse;
import com.snowflakes.rednose.entity.Seal;
import lombok.Builder;
import org.springframework.data.domain.Slice;
import java.util.List;

public class ShowMySealLikesResponse {

    private boolean hasNext;
    private List<SealResponse> contents;

    @Builder
    public ShowMySealLikesResponse(boolean hasNext, List<SealResponse> contents) {
        this.hasNext = hasNext;
        this.contents = contents;
    }

    public static ShowMySealLikesResponse from(Slice<Seal> seals) {
        return ShowMySealLikesResponse.builder()
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
