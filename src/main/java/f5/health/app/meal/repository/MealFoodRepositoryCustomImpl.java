package f5.health.app.meal.repository;

import f5.health.app.meal.entity.MealFood;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MealFoodRepositoryCustomImpl implements MealFoodRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public int[] saveAllBatch(final List<MealFood> mealFoods) {

        return jdbcTemplate.batchUpdate(
                "INSERT INTO MEAL_FOOD " +
                        "(MEAL_ID, FOOD_CODE, COUNT) " +
                        "VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MealFood mealFood = mealFoods.get(i);
                        ps.setLong(1, mealFood.getMeal().getId());
                        ps.setString(2, mealFood.getFood().getFoodCode());
                        ps.setFloat(3, (float) mealFood.getCount());
                    }

                    @Override
                    public int getBatchSize() {
                        return mealFoods.size();
                    }
                });
    }

}
