package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FOOD_NUTRIENT")
public class FoodNutrient {

    @Id
    @Column(name = "FOOD_CODE")
    private String foodCode;

    @Column(name = "FOOD_NAME", unique = true)
    private String foodName;

    @Column(name = "FOOD_TYPE")
    private String foodType;

    @Column(name = "KCAL")
    private int kcal;

    @Column(name = "NATRIUM")
    private int natrium;

    @Column(name = "CARBOHYDRATE")
    private double carbohydrate;

    @Column(name = "SUGAR")
    private double sugar;

    @Column(name = "PROTEIN")
    private double protein;

    @Column(name = "FAT")
    private double fat;

    @Column(name = "NUT_CON_STD_QUA")
    private String nutConStdQua;

    @Column(name = "FOOD_WEIGHT")
    private String foodWeight;
}
