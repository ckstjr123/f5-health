package f5.health.app.exception.healthreport;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

import static f5.health.app.entity.Meal.MENU_LIMIT_SIZE_PER_MEAL;

@Getter
public enum HealthReportErrorCode implements ErrorCode {

    NOT_FOUND_REPORT("기록된 리포트가 없습니다.");

    private final String code;
    private final String message;

    HealthReportErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
