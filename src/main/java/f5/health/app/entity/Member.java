package f5.health.app.entity;

import f5.health.app.constant.Badge;
import f5.health.app.constant.BloodType;
import f5.health.app.constant.Gender;
import f5.health.app.constant.Role;
import f5.health.app.entity.base.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "OAUTH_ID")
    private String oauthId;

    @Column(name = "EMAIL", unique = true)
    private String email; // oauth 검증을 마치고 얻은 이메일

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "BADGE")
    @Enumerated(EnumType.STRING)
    private Badge badge;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "HEIGHT")
    private int height;

    @Column(name = "WEIGHT")
    private int weight;

    @Column(name = "BLOOD_TYPE")
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name = "DAY_SMOKING_AVG")
    private int daySmokingAvg; // 0이면 비흡연자

    @Column(name = "WEEK_ALCOHOL_AVG")
    private int weekAlcoholAvg;

    @Column(name = "WEEK_EXERCISE_FREQ")
    private int weekExerciseFreq;

    
    /** 회원 생성 메서드 */
    public static Member createMember(String oauthId, String email, String username, Role role, MemberCheckUp memberCheckUp) {
        Member member = new Member();
        member.oauthId = oauthId;
        member.email = email;
        member.nickname = username;
        member.role = role;
        member.badge = Badge.BEGINNER;
        memberCheckUp.applyTo(member);
        return member;
    }


    @Schema(description = "회원 설문 정보 전달받는 VO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberCheckUp {

        @Schema(description = "생년월일", example = "2000-04-18", requiredMode = REQUIRED)
        @NotNull(message = "생년월일을 입력해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        @Schema(description = "성별", example = "남자", requiredMode = REQUIRED)
        @NotNull(message = "성별을 입력해주세요.")
        private Gender gender;

        @Schema(description = "키", example = "173", requiredMode = REQUIRED)
        @Range(min = 100, max = 230)
        private int height;

        @Schema(description = "몸무게", example = "65", requiredMode = REQUIRED)
        @Range(min = 20, max = 200)
        private int weight;

        @Schema(description = "혈액형", example = "AB", requiredMode = REQUIRED)
        @NotNull(message = "혈액형을 입력해주세요.")
        private BloodType bloodType;

        @Schema(description = "일평균 흡연량(개비)", example = "8", requiredMode = REQUIRED)
        @Range(min = 0, max = 30)
        private int daySmokingAvg;

        @Schema(description = "주평균 알코올 섭취량(잔)", example = "6", requiredMode = REQUIRED)
        @Range(min = 0, max = 50)
        private int weekAlcoholAvg;

        @Schema(description = "주평균 운동 횟수", example = "3", requiredMode = REQUIRED)
        @Range(min = 0, max = 7)
        private int weekExerciseFreq;

        /** 테스트용 생성자 */
        public MemberCheckUp(LocalDate birthDate, Gender gender, int height, int weight, BloodType bloodType, int daySmokingAvg, int weekAlcoholAvg, int weekExerciseFreq) {
            this.birthDate = birthDate;
            this.gender = gender;
            this.height = height;
            this.weight = weight;
            this.bloodType = bloodType;
            this.daySmokingAvg = daySmokingAvg;
            this.weekAlcoholAvg = weekAlcoholAvg;
            this.weekExerciseFreq = weekExerciseFreq;
        }


        /** 회원 설문 정보 반영 */
        private void applyTo(Member member) {
            member.birthDate = this.birthDate;
            member.gender = this.gender;
            member.height = this.height;
            member.weight = this.weight;
            member.bloodType = this.bloodType;
            member.daySmokingAvg = this.daySmokingAvg;
            member.weekAlcoholAvg = this.weekAlcoholAvg;
            member.weekExerciseFreq = this.weekExerciseFreq;
        }
    }

}
