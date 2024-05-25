package com.snowflakes.rednose.dto;

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
}
