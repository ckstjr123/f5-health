package f5.health.app.food.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FOOD")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_ID")
    private Long id;

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

    @Column(name = "SERVING_SIZE")
    private double servingSize;

    @Column(name = "UNIT")
    private String unit;

    @Builder
    private Food(String foodName, String foodType, int kcal, int natrium, double carbohydrate, double sugar, double protein, double fat, double servingSize, String unit) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.kcal = kcal;
        this.natrium = natrium;
        this.carbohydrate = carbohydrate;
        this.sugar = sugar;
        this.protein = protein;
        this.fat = fat;
        this.servingSize = servingSize;
        this.unit = unit;
    }
}
