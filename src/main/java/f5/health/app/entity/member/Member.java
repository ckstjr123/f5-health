package f5.health.app.entity.member;

import f5.health.app.constant.AlcoholType;
import f5.health.app.constant.member.BloodType;
import f5.health.app.constant.member.Gender;
import f5.health.app.constant.member.Role;
import f5.health.app.constant.member.badge.Badge;
import f5.health.app.entity.base.BaseTimeEntity;
import f5.health.app.entity.healthreport.PromptCompletion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.Period;

import static f5.health.app.constant.member.Gender.MALE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    public static final int MAX_NICKNAME_LENGTH = 15;
    public static final int DAYS_IN_WEEK = 7;
    private static final int ONE_CIGARETTE_PRICE = 225;

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

    @Column(name = "TOTAL_HEALTH_LIFE_SCORE")
    private long totalHealthLifeScore; // 생활 점수 누적

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

    @Column(name = "DAY_SMOKE_CIGARETTES")
    private int daySmokeCigarettes; // 일일 흡연량(0이면 비흡연자)
    @Column(name = "SMOKING_SAVED_MONEY")
    private int smokingSavedMoney; // 흡연 절약 금액

    @Column(name = "WEEK_ALCOHOL_DRINKS")
    private int weekAlcoholDrinks; // 주평균 음주량(ml)
    @Column(name = "ALCOHOL_SAVED_MONEY")
    private int alcoholSavedMoney; // 음주 절약 금액

    @Column(name = "WEEK_EXERCISE_FREQ")
    private int weekExerciseFrequency;

    @Column(name = "HEALTH_ITEMS_RECOMMEND")
    private String healthItemsRecommend; // 절약 금액에 대한 gpt 건강 아이템 추천 결과

    /** 회원 생성 메서드 */
    public static Member createMember(String oauthId, String email, String nickname, Role role, MemberCheckUp memberCheckUp) {
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

    /** 키, 성별, 몸무게, 활동량에 따른 개인별 권장 칼로리 */
    public int getRecommendedCalories() {
        return (int) Math.round(calculateBmr() * getActivityFactor());
    }

    public double calculateBmr() {
        int age = this.getAge();
        return (this.gender == MALE) ? (66.47 + (13.75 * weight) + (5 * height) - (6.76 * age))
                : (655.1 + (9.56 * weight) + (1.85 * height) - (4.68 * age));
    }
    private double getActivityFactor() {
        if (weekExerciseFrequency >= 5) {
            return 1.55;
        } else if (weekExerciseFrequency >= 2) {
            return 1.375;
        }
        return 1.2;
    }

    private void setBadge(Badge badge) {
        this.badge = badge;
    }

    /** 회원 점수 누적 및 배지 체크 */
    public void addHealthLifeScore(final int score) {
        this.totalHealthLifeScore += score;
        this.setBadge(Badge.fromTotalScore(totalHealthLifeScore));
    }


    public boolean isSmoker() {
        return (daySmokeCigarettes > 0); // 일일 흡연량이 1개비 이상이면 흡연자
    }

    public void accumulateSmokingSavedMoneyForDay(final int smokedCigarettes) {
        if (isSmoker()) {
            this.smokingSavedMoney += (daySmokeCigarettes - smokedCigarettes) * ONE_CIGARETTE_PRICE;
        }
    }


    public boolean isAlcoholDrinker() {
        return (weekAlcoholDrinks > 0); // 주 알코올 섭취량이 0ml 넘으면 음주자
    }

    public void accumulateAlcoholSavedMoneyForDay(@Nullable AlcoholConsumptionResult alcoholConsumptionResult) {
        if (!isAlcoholDrinker()) {
            return;
        }

        double expectedAlcoholCostPerDay = ((double) weekAlcoholDrinks / DAYS_IN_WEEK) * AlcoholType.AVERAGE_MAJOR_ALCOHOL_PRICE_PER_ML; // 실제 선호하는 주류로 대체 가능
        double actualAlcoholCost = (alcoholConsumptionResult != null) ? alcoholConsumptionResult.getTotalAlcoholCost() : 0;

        this.alcoholSavedMoney += (int) Math.round(expectedAlcoholCostPerDay - actualAlcoholCost);
    }


    public int getTotalSavedMoney() {
        return (smokingSavedMoney + alcoholSavedMoney);
    }

    public void updateHealthItemsRecommend(PromptCompletion healthItemsRecommend) {
        this.healthItemsRecommend = healthItemsRecommend.getContent();
    }


    public void updateProfile(MemberUpdateRequest updateParam) {
        this.nickname = updateParam.getNickname();
        this.height = updateParam.getHeight();
        this.weight = updateParam.getWeight();
        this.daySmokeCigarettes = updateParam.getDaySmokeCigarettes();
        this.weekAlcoholDrinks = updateParam.getWeekAlcoholDrinks();
        this.weekExerciseFrequency = updateParam.getWeekExerciseFrequency();
    }


    @Schema(description = "회원가입 설문 정보")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberCheckUp {

        public static final int DAILY_MAX_CIGARETTES = 40;
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

        @Schema(description = "일일 흡연량(개비)", example = "8", requiredMode = REQUIRED)
        @Range(min = 0, max = DAILY_MAX_CIGARETTES)
        private int daySmokeCigarettes;

        @Schema(description = "주평균 알코올 섭취량(ml)", example = "1200", requiredMode = REQUIRED)
        @Range(min = 0, max = WEEK_MAX_ALCOHOL_ML)
        private int weekAlcoholDrinks;

        @Schema(description = "주평균 운동 횟수", example = "3", requiredMode = REQUIRED)
        @Range(min = 0, max = DAYS_IN_WEEK)
        private int weekExerciseFrequency;

        /** 설문 정보 반영 */
        private void applyTo(Member member) {
            member.birthDate = this.birthDate;
            member.gender = this.gender;
            member.height = this.height;
            member.weight = this.weight;
            member.bloodType = this.bloodType;
            member.daySmokeCigarettes = this.daySmokeCigarettes; // 흡연
            member.weekAlcoholDrinks = this.weekAlcoholDrinks; // 음주
            member.weekExerciseFrequency = this.weekExerciseFrequency;
        }
    }
    
}
