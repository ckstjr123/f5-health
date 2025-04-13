package f5.health.app.entity;

import f5.health.app.exception.MenuSizeOverflowException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "HEALTH_REPORT")
public class HealthReport {

    public static final int MENU_LIMIT_SIZE_PER_DAY = 15; // 하루 식단에 등록 가능한 메뉴 최대 개수

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HEALTH_REPORT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "HEALTH_LIFE_SCORE")
    private int healthLifeScore;

    @Column(name = "WATER_INTAKE")
    private int waterIntake;

    @Column(name = "EAT_FOODS")
    private List<String> eatFoods = new ArrayList<>(); // db에 ["치킨", "피자", "콜라"]와 같이 저장됨

    @Column(name = "SMOKED_CIGARETTES")
    private int smokedCigarettes;

    @Column(name = "ALCOHOL_DRINKS")
    private int alcoholDrinks;

    @Column(name = "FITNESS_ADVICE")
    private String fitnessAdvice;

    @Column(name = "FOOD_ADVICE")
    private String foodAdvice;
    
    // startDate ~ endDate: 건강 관측 기간 //
    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "REPORTED_AT")
    private LocalDateTime reportedAt; // 제출 시각



    // ------------------------- 비즈니스 로직 ------------------------- //

    public void addWaterIntake(int ml) {
        this.waterIntake += ml; // 동시에 누르는 경우 없다고 가정
    }

    public void recordEatFoods(Set<String> eatFoods) {
        if ((this.eatFoods.size() + eatFoods.size()) > MENU_LIMIT_SIZE_PER_DAY) {
            throw new MenuSizeOverflowException(MENU_LIMIT_SIZE_PER_DAY);
        }

        this.eatFoods.addAll(eatFoods);
    }

}
