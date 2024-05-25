package com.snowflakes.rednose.dto.stampcraft;

import lombok.Getter;

@Getter
public class PaintStampRequest {

    private int x;
    private int y;
    private String color;

    protected PaintStampRequest() {
    }

}
