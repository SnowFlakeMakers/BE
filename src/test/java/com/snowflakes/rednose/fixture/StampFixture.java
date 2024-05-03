package com.snowflakes.rednose.fixture;

import com.snowflakes.rednose.entity.Stamp;
import java.time.LocalDateTime;

public class StampFixture {

    private Long id;
    private String name = "우표";
    private String imageUrl = "https://image.com/virtual.png";
    private LocalDateTime createdAt;

    public static StampFixture builder() {
        return new StampFixture();
    }

    public StampFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StampFixture nickname(String name) {
        this.name = name;
        return this;
    }

    public StampFixture imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public StampFixture createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Stamp build() {
        return Stamp.builder().id(id).name(name).imageUrl(imageUrl).createdAt(createdAt).build();
    }
}
