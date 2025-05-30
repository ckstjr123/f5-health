package f5.health.app.config;

import f5.health.app.constant.AlcoholType;
import f5.health.app.constant.EnumModelMapper;
import f5.health.app.constant.meal.MealType;
import f5.health.app.constant.member.badge.Badge;
import f5.health.app.constant.member.badge.BadgeEnumModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EnumModelMapper enumMapper() {
        EnumModelMapper enumMapper = new EnumModelMapper();
        enumMapper.put(Badge.class, BadgeEnumModel.class);
        enumMapper.put(MealType.class);
        enumMapper.put(AlcoholType.class);
        return enumMapper;
    }
}
