package com.snowflakes.rednose.support.fixture;

import com.snowflakes.rednose.entity.Stamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StampFixture {

    private final String name = "우표";
    private final String imageUrl = "https://image.com/virtual.png";
    private LocalDateTime createdAt = LocalDateTime.now();
    private int numberOfLikes;

    public static StampFixture builder() {
        return new StampFixture();
    }

    public StampFixture createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public StampFixture numberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
        return this;
    }

    public Stamp build() {
        return Stamp.builder().name(name).imageUrl(imageUrl).createdAt(createdAt).numberOfLikes(numberOfLikes)
                .build();
    }
}
