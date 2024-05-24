package com.snowflakes.rednose.dto.seal;

import static lombok.AccessLevel.PROTECTED;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor
public class AssignSealNameRequest {
    @NotBlank(message = "씰 이름은 빈칸일 수 없습니다")
    @Length(max = 255, min = 1, message = "씰 이름은 1자 이상 255자 이하여야 합니다")
    private String name;

    @NotBlank(message = "씰 id는 빈칸일 수 없습니다")
    @Min(value = 1, message = "씰 id는 0 이하일 수 없습니다")
    private Long sealId;
}
