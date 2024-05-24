package com.snowflakes.rednose.dto.seal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AssignSealNameResponse {
    private Long sealId;
    private String image;
    private String name;
}
