package f5.health.app.controller.meal;

import f5.health.app.constant.EnumModel;
import f5.health.app.constant.EnumModelMapper;
import f5.health.app.constant.meal.MealType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController implements MealApiDocs {

    private final EnumModelMapper enumMapper;
    
    @GetMapping("/types")
    public List<? extends EnumModel> mealTypes() {
        return enumMapper.get(MealType.class);
    }

    /** mealId 식단 조회 */

}
