package f5.health.app.meal.controller;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "식사 기록 저장")
public record CreateMealResponse(@Schema(description = "저장된 식단 id", example = "1") Long mealId) {
}