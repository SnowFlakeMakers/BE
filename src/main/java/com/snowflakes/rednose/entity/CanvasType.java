package com.snowflakes.rednose.entity;

public enum CanvasType {
    SMALL(16),
    MEDIUM(32),
    LARGE(64);

    private final int length;

    CanvasType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
