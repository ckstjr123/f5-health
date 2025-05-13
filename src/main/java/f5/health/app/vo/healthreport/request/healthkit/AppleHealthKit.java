package f5.health.app.vo.healthreport.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "사용자 애플 건강 앱 데이터", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppleHealthKit {



    // Period class
    // Activity class
    // sleepAnalysis
    // vitalSigns
    // Workouts class

    // CustomHealthKit



}
