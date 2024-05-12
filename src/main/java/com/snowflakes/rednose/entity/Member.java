package com.snowflakes.rednose.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_id", nullable = false)
    private Long socialId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "image")
    private String image;

    @Column(name = "usable", nullable = false)
    private boolean usable;

    protected Member() {
    }

    @Builder
    private Member(Long id, Long socialId, String nickname, String image, boolean usable) {
        this.id = id;
        this.socialId = socialId;
        this.nickname = nickname;
        this.image = image;
        this.usable = usable;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
