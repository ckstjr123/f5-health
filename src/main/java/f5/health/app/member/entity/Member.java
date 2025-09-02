package f5.health.app.member.entity;

import f5.health.app.member.constant.BloodType;
import f5.health.app.member.constant.Gender;
import f5.health.app.member.constant.Role;
import f5.health.app.member.constant.Badge;
import f5.health.app.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

import static f5.health.app.member.constant.Gender.MALE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    public static final int MAX_NICKNAME_LENGTH = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "OAUTH_ID")
    private String oauthId;

    @Column(name = "EMAIL", unique = true)
    private String email; // OAuth 액세스 토큰 검증을 마치고 얻은 이메일

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "TOTAL_POINT")
    private long totalPoint; // 포인트 누적

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


    /** 회원 생성 메서드 */
    public static Member createMember(String oauthId, String email, String nickname, Role role, CheckUp memberCheckUp) {
        Member member = new Member();
        member.oauthId = oauthId;
        member.email = email;
        member.nickname = nickname;
        member.role = role;
        member.badge = Badge.BEGINNER;
        memberCheckUp.applyTo(member);
        return member;
    }


    public int getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public double calculateBmr() {
        int age = getAge();
        return (this.gender == MALE) ? (66.47 + (13.75 * weight) + (5 * height) - (6.76 * age))
                : (655.1 + (9.56 * weight) + (1.85 * height) - (4.68 * age));
    }

    private void setBadge(Badge badge) {
        this.badge = badge;
    }

    /** 포인트 누적 및 배지 체크 */
    public void addPoint(final int point) {
        this.totalPoint += point;
        this.setBadge(Badge.fromTotalPoint(totalPoint));
    }

    public void updateProfile(final String nickname, final int height, final int weight) {
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
    }


    @Schema(description = "회원가입 설문 정보")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CheckUp {

        public static final int DAILY_MAX_WATER_ML = 5000;
        public static final int DAILY_MAX_CIGARETTES = 40;
        public static final int DAILY_MAX_ALCOHOL_ML = 1500;
        public static final int WEEK_MAX_ALCOHOL_ML = 10000;

        @Schema(description = "생년월일", example = "2000-04-18", requiredMode = REQUIRED)
        @NotNull(message = "생년월일을 입력해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        @Schema(description = "성별", example = "MALE", requiredMode = REQUIRED)
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

        @Builder(toBuilder = true)
        private CheckUp(LocalDate birthDate, Gender gender, int height, int weight, BloodType bloodType) {
            this.birthDate = birthDate;
            this.gender = gender;
            this.height = height;
            this.weight = weight;
            this.bloodType = bloodType;
        }

        /** 설문 정보 반영 */
        private void applyTo(Member member) {
            member.birthDate = this.birthDate;
            member.gender = this.gender;
            member.height = this.height;
            member.weight = this.weight;
            member.bloodType = this.bloodType;
        }
    }
    
}
