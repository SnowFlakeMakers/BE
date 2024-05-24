package com.snowflakes.rednose.dto.seal;


import static lombok.AccessLevel.PROTECTED;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class MakeSealRequest {
    @NotBlank(message = "씰 이미지 경로가 비었습니다")
    private String image;
}
