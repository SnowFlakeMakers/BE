package com.snowflakes.rednose.entity;

import lombok.Builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StampCraft {

    private Member host;

    private CanvasType canvasType;

    private Map<String, Member> members = new ConcurrentHashMap<>();

    private String[][] stamp;

    protected StampCraft() {
    }

    @Builder
    public StampCraft(Member host, CanvasType canvasType, Map<String, Member> members, String[][] stamp) {
        this.host = host;
        this.canvasType = canvasType;
        this.members = members;
        this.stamp = new String[canvasType.getLength()][canvasType.getLength()];
    }

    public Member getHost() {
        return host;
    }

    public CanvasType getCanvasType() {
        return canvasType;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    public String[][] getStamp() {
        return stamp;
    }

    public void paint(int x, int y, String color) {
        if(color.isEmpty()) throw new RuntimeException();
        stamp[x][y] = color;
    }
}
