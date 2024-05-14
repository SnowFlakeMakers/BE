package com.snowflakes.rednose.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;

@Entity
public class StampCraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @Enumerated(value = EnumType.STRING)
    private CanvasType canvasType;

    protected StampCraft() {
    }

    @Builder
    public StampCraft(Long id, Member host, CanvasType canvasType) {
        this.id = id;
        this.host = host;
        this.canvasType = canvasType;
    }

    public Long getId() {
        return id;
    }

    public Member getHost() {
        return host;
    }

    public CanvasType getCanvasType() {
        return canvasType;
    }
}
