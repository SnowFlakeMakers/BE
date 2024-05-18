package com.snowflakes.rednose.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;

@Entity
public class StampRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false)
    private Stamp stamp;

    @Builder
    private StampRecord(Long id, Member member, Stamp stamp) {
        this.id = id;
        this.member = member;
        this.stamp = stamp;
    }

    protected StampRecord() {
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Stamp getStamp() {
        return stamp;
    }
}
