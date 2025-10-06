package f5.health.app.common.config;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.meal.constant.MealType;
import f5.health.app.member.constant.Badge;
import f5.health.app.member.controller.BadgeEnumModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public EnumModelMapper enumMapper() {
        EnumModelMapper enumMapper = new EnumModelMapper();
        enumMapper.put(Badge.class, BadgeEnumModel.class);
        enumMapper.put(MealType.class);
        enumMapper.put(AlcoholType.class);
        return enumMapper;
    }
}
