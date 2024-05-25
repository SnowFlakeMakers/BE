package com.snowflakes.rednose.dto;

import com.snowflakes.rednose.entity.Seal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MakeSealResponse {
    private Long sealId;
    private String image;
    private String name;

    public static MakeSealResponse from(Seal seal) {
        return MakeSealResponse.builder().sealId(seal.getId()).image(seal.getImageUrl()).name(seal.getName()).build();
    }
}
