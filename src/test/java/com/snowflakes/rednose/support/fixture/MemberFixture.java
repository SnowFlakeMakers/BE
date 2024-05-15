package com.snowflakes.rednose.support.fixture;

import com.snowflakes.rednose.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberFixture {

    private final String image = "https://image.com/virtual.png";
    private final String nickname = "미친석촌호수맨";
    private final boolean usable = true;
    private Long id;
    private Long socialId = 123456789L;

    public static MemberFixture builder() {
        return new MemberFixture();
    }

    public MemberFixture socialId(Long socialId) {
        this.socialId = socialId;
        return this;
    }

    public Member build() {
        return Member.builder().image(image).nickname(nickname).usable(usable).socialId(socialId).build();
    }
}
