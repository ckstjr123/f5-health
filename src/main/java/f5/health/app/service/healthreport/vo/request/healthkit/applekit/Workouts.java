package f5.health.app.service.healthreport.vo.request.healthkit.applekit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Schema(description = "운동", nullable = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Workouts {

    @Schema(description = "사용자 운동 유형", example = "walking")
    private Set<String> workoutTypes;

    public static Workouts empty() {
        return new Workouts();
    }

}
