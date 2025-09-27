package f5.health.app.member.entity.vo;

import f5.health.app.member.constant.BloodType;
import f5.health.app.member.constant.Gender;
import f5.health.app.member.validation.Height;
import f5.health.app.member.validation.Weight;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "회원가입 설문 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCheckUp {

    @Schema(description = "생년월일", example = "2000-04-18", requiredMode = REQUIRED)
    @NotNull(message = "생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(description = "성별", example = "MALE", requiredMode = REQUIRED)
    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;

    @Schema(description = "키", example = "173", requiredMode = REQUIRED)
    @Height
    private double height;

    @Schema(description = "몸무게", example = "65", requiredMode = REQUIRED)
    @Weight
    private double weight;

    @Schema(description = "혈액형", example = "AB", requiredMode = REQUIRED)
    @NotNull(message = "혈액형을 입력해주세요.")
    private BloodType bloodType;

    @Builder(toBuilder = true)
    private MemberCheckUp(LocalDate birthDate, Gender gender, double height, double weight, BloodType bloodType) {
        this.birthDate = birthDate;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
    }
}