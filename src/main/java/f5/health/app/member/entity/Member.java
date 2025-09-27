package f5.health.app.member.entity;

import f5.health.app.common.BaseEntity;
import f5.health.app.member.constant.Badge;
import f5.health.app.member.constant.BloodType;
import f5.health.app.member.constant.Gender;
import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.vo.MemberCheckUp;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.Period;

import static f5.health.app.member.constant.Gender.MALE;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    public static final int MAX_NICKNAME_LENGTH = 15;
    public static final double MIN_HEIGHT = 100, MAX_HEIGHT = 230;
    public static final double MIN_WEIGHT = 20, MAX_WEIGHT = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "OAUTH_ID")
    private String oauthId;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "NICKNAME", length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "TOTAL_POINT")
    private long totalPoint; // 누적 포인트

    @Column(name = "BADGE")
    @Enumerated(EnumType.STRING)
    private Badge badge;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "HEIGHT")
    private double height;

    @Column(name = "WEIGHT")
    private double weight;

    @Column(name = "BLOOD_TYPE")
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Builder(builderMethodName = "createMember")
    private static Member newInstance(String oauthId, String email, String nickname, Role role, MemberCheckUp checkUp) {
        Member member = new Member();
        member.oauthId = oauthId;
        member.email = email;
        member.nickname = nickname;
        member.role = role;
        member.badge = Badge.BEGINNER;
        member.apply(checkUp);
        return member;
    }

    /** 설문 결과 반영 */
    private void apply(MemberCheckUp checkUp) {
        this.birthDate = checkUp.getBirthDate();
        this.gender = checkUp.getGender();
        this.height = checkUp.getHeight();
        this.weight = checkUp.getWeight();
        this.bloodType = checkUp.getBloodType();
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

    public void updateProfile(final String nickname, final double height, final double weight) {
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
    }

}
