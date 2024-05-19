package com.snowflakes.rednose.entity;

import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
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

    @Column(name = "refresh_token")
    private String refreshToken;

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

    public void storeRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new UnAuthorizedException(AuthErrorCode.MALFORMED);
        }
        this.refreshToken = refreshToken;
    }

    public void expireRefreshToken() {
        this.refreshToken = null;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static Member from(UserInfo userInfo) {
        return Member.builder().socialId(userInfo.getId()).usable(true)
                .image(userInfo.getKakaoAccount().getProfile().getProfileImageUrl()).build();
    }

    public void signIn(String nickname) {
        if (nickname == null) {
            throw new BadRequestException(MemberErrorCode.NULL_NICKNAME);
        }
        this.nickname = nickname;
    }
}
