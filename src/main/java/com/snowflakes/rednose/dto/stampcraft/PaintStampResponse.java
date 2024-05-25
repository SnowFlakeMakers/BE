package com.snowflakes.rednose.dto.stampcraft;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaintStampResponse {

    private MessageType type;
    private int x;
    private int y;
    private String color;

    @Builder
    public PaintStampResponse(MessageType type, int x, int y, String color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public static PaintStampResponse from(PaintStampRequest request) {
        return PaintStampResponse.builder()
                .type(MessageType.PAINT)
                .x(request.getX())
                .y(request.getY())
                .color(request.getColor())
                .build();
    }

}
