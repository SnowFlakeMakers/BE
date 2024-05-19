package com.snowflakes.rednose.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInRequest {

    @NotBlank(message = "닉네임은 빈칸일 수 없습니다")
    private String nickname;
}
