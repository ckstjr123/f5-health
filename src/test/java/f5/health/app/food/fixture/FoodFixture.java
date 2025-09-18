package f5.health.app.food.fixture;

import f5.health.app.food.entity.Food;

import java.util.List;

public class FoodFixture {

    public static List<Food> createFoods() {
        return List.of(
                FoodFixture.createBasicFood("G123-226020200-1391", "김밥"),
                FoodFixture.createHighProteinFood("G123-226020200-1392", "요거트"),
                FoodFixture.createHighCalorieFood("G123-226020200-1393", "떡볶이"),
                FoodFixture.createLowSugarFood("G123-226020200-1394", "제로 콜라")
        );
    }

    public static Food createBasicFood(String foodCode, String foodName) {
        return Food.builder()
                .foodCode(foodCode)
                .foodName(foodName)
                .kcal(250)
                .carbohydrate(30.0)
                .protein(7.0)
                .fat(8.0)
                .sugar(5.0)
                .natrium(200)
                .totalGram(150)
                .build();
    }

    public static Food createHighProteinFood(String foodCode, String foodName) {
        return Food.builder()
                .foodCode(foodCode)
                .foodName(foodName)
                .kcal(150)
                .carbohydrate(1.0)
                .protein(31.0)
                .fat(3.5)
                .sugar(0.5)
                .natrium(70)
                .totalGram(120)
                .build();
    }

    public static Food createHighCalorieFood(String foodCode, String foodName) {
        return Food.builder()
                .foodCode(foodCode)
                .foodName(foodName)
                .kcal(700)
                .carbohydrate(45.0)
                .protein(6.0)
                .fat(25.0)
                .sugar(30.0)
                .natrium(300)
                .totalGram(100)
                .build();
    }

    public static Food createLowCalorieFood(String foodCode, String foodName) {
        return Food.builder()
                .foodCode(foodCode)
                .foodName(foodName)
                .kcal(20)
                .carbohydrate(10.0)
                .protein(2.0)
                .fat(1.0)
                .sugar(3.0)
                .natrium(10)
                .totalGram(50)
                .build();
    }

    public static Food createLowSugarFood(String foodCode, String foodName) {
        return Food.builder()
                .foodCode(foodCode)
                .foodName(foodName)
                .kcal(5)
                .carbohydrate(0.5)
                .protein(0.0)
                .fat(1.0)
                .sugar(0.1)
                .natrium(10)
                .totalGram(350)
                .build();
    }

}
