package com.snowflakes.rednose.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.snowflakes.rednose.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfo {
    private Long id;
    private KaKaoAccount kakaoAccount;

    public Member toMember(String randomNickname) {
        return Member.builder().socialId(id).usable(true).nickname(randomNickname)
                .image(kakaoAccount.getProfile().getProfileImageUrl()).build();
    }
}
