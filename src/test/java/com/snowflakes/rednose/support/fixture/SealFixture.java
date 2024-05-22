package com.snowflakes.rednose.support.fixture;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import java.time.LocalDateTime;

public class SealFixture {
    private Long id;
    private Member member;
    private String name = "씰";
    private String imageUrl = "https://image.com/virtual.png";
    private LocalDateTime createdAt = LocalDateTime.now();
    private int numberOfLikes;

    public static SealFixture builder() {
        return new SealFixture();
    }

    public SealFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SealFixture member(Member member) {
        this.member = member;
        return this;
    }

    public SealFixture name(String name) {
        this.name = name;
        return this;
    }

    public SealFixture imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public SealFixture createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SealFixture numberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
        return this;
    }

    public Seal build() {
        return Seal.builder()
                .id(id)
                .member(member)
                .name(name)
                .imageUrl(imageUrl)
                .createdAt(createdAt)
                .numberOfLikes(numberOfLikes)
                .build();
    }

}
