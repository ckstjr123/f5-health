package f5.health.app.food.fixture;

import f5.health.app.food.entity.Food;

import java.util.List;

public class FoodFixture {

    public static List<Food> createFoods() {
        return List.of(
                FoodFixture.createBasicFood("잡곡밥"),
                FoodFixture.createHighProteinFood("닭가슴살")
        );
    }

    public static Food createBasicFood(String foodName) {
        return Food.builder()
                .foodName(foodName)
                .kcal(250)
                .carbohydrate(30.0)
                .protein(7.0)
                .fat(8.0)
                .sugar(5.0)
                .natrium(200)
                .servingSize(150)
                .build();
    }

    public static Food createHighProteinFood(String foodName) {
        return Food.builder()
                .foodName(foodName)
                .kcal(150)
                .carbohydrate(1.0)
                .protein(31.0)
                .fat(3.5)
                .sugar(0.5)
                .natrium(70)
                .servingSize(120)
                .build();
    }

    public static Food createHighCalorieFood(String foodName) {
        return Food.builder()
                .foodName(foodName)
                .kcal(700)
                .carbohydrate(45.0)
                .protein(6.0)
                .fat(25.0)
                .sugar(30.0)
                .natrium(300)
                .servingSize(100)
                .build();
    }

    public static Food createLowCalorieFood(String foodName) {
        return Food.builder()
                .foodName(foodName)
                .kcal(20)
                .carbohydrate(10.0)
                .protein(2.0)
                .fat(1.0)
                .sugar(3.0)
                .natrium(10)
                .servingSize(50)
                .build();
    }

    public static Food createLowSugarFood(String foodName) {
        return Food.builder()
                .foodName(foodName)
                .kcal(5)
                .carbohydrate(0.5)
                .protein(0.0)
                .fat(1.0)
                .sugar(0.1)
                .natrium(10)
                .servingSize(350)
                .build();
    }

}
