package f5.health.app.vo.member.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdateMemberInfoRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @Min(100)
    @Max(230)
    private int height;

    @Min(20)
    @Max(200)
    private int weight;

    @Min(0)
    @Max(50)
    private int weekAlcoholDrinks;

    @Min(0)
    @Max(30)
    private int daySmokeCigarettes;

    @Min(0)
    @Max(7)
    private int weekExerciseFrequency;
}

