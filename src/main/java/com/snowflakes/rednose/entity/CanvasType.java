package com.snowflakes.rednose.entity;

public enum CanvasType {
    SMALL(16),
    MEDIUM(32),
    LARGE(64);

    private final int size;

    CanvasType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
