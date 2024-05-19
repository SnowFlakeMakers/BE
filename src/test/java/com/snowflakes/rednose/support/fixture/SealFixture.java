package com.snowflakes.rednose.support.fixture;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SealFixture {

    private final String name = "씰";
    private final String imageUrl = "https://image.com/virtual.png";
    private LocalDateTime createdAt = LocalDateTime.now();
    private int numberOfLikes;
    private Long id;
    private Member member;

    public static SealFixture builder() {
        return new SealFixture();
    }

    public SealFixture member(Member member) {
        this.member = member;
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

    public SealFixture id(Long id) {
        this.id = id;
        return this;
    }

    public Seal build() {
        return Seal.builder().name(name).imageUrl(imageUrl).createdAt(createdAt).numberOfLikes(numberOfLikes).id(id)
                .build();
    }
}
