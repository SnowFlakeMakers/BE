package com.snowflakes.rednose.entity;

public enum CanvasType {
    SMALL(8),
    MEDIUM(10),
    LARGE(12);

    private final int size;

    CanvasType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
