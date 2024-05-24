package com.snowflakes.rednose.dto.seal;


import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class MakeSealRequest {
    private String image;
}
