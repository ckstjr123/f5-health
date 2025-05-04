package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 식사 항목 엔티티 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEAL_FOOD")
public class MealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_FOOD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //양방향 매핑
    @JoinColumn(name = "MEAL_ID")
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOOD_CODE")
    private Food food;

    @Column(name = "COUNT")
    private int count; // 해당 음식 섭취 수량
}
