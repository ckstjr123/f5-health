package f5.health.app.entity;

import f5.health.app.constant.Badge;
import f5.health.app.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "OAUTH_ID")
    private String oauthId; // apple 'sub'

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "BADGE")
    @Enumerated(EnumType.STRING)
    private Badge badge;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "HEIGHT")
    private int height;

    @Column(name = "WEIGHT")
    private int weight;

    @Column(name = "BLOOD_TYPE")
    private String bloodType;

    @Column(name = "IS_SMOKER")
    private boolean isSmoker;

    @Column(name = "DAY_SMOKING_AVG")
    private int daySmokingAvg;

    @Column(name = "WEEK_ALCOHOL_AVG")
    private int weekAlcoholAvg;

    @Column(name = "WEEK_EXERCISE_FREQ")
    private int weekExerciseFreq;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // 정적 팩토리 메서드..
}
