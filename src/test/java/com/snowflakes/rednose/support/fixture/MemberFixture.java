package com.snowflakes.rednose.support.fixture;

import com.snowflakes.rednose.entity.Member;
import java.util.Random;

public class MemberFixture {

    private Long id;
    private Long socialId = new Random().nextLong();
    private String nickname = "rlfrkdms1";
    private String image = "https://image.com/virtual.png";
    private boolean usable = true;

    public static MemberFixture builder() {
        return new MemberFixture();
    }

    public MemberFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberFixture socialId(Long socialId) {
        this.socialId = socialId;
        return this;
    }

    public MemberFixture nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public MemberFixture image(String image) {
        this.image = image;
        return this;
    }

    public MemberFixture usable(boolean usable) {
        this.usable = usable;
        return this;
    }

    public Member build() {
        return Member.builder().id(id).socialId(socialId).nickname(nickname).image(image).usable(usable).build();
    }
}
