package com.snowflakes.rednose.entity;

import lombok.Builder;
import java.util.ArrayList;
import java.util.List;
public class StampCraft {

    private Member host;

    private CanvasType canvasType;

    private List<Member> members = new ArrayList<>();

    private String[][] stamp;

    protected StampCraft() {
    }

    @Builder
    public StampCraft(Member host, CanvasType canvasType, List<Member> members, String[][] stamp) {
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

    public List<Member> getMembers() {
        return members;
    }

    public String[][] getStamp() {
        return stamp;
    }

    public void paint(int x, int y, String color) {
        if(color.isEmpty()) throw new RuntimeException();
        stamp[x][y] = color;
    }

    public void enter(Member member) {
        members.add(member);
    }

    public void quit(Member member) {
        members.remove(member);
    }
}
