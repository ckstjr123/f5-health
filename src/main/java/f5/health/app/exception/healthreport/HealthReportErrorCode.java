package f5.health.app.exception.healthreport;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

import static f5.health.app.entity.HealthReport.MENU_LIMIT_SIZE_PER_DAY;

@Getter
public enum HealthReportErrorCode implements ErrorCode {

    EXCEEDED_MAX_MENU_COUNT("하루에 기록 가능한 메뉴 최대 개수는 " + MENU_LIMIT_SIZE_PER_DAY + "개입니다.");
    
    private final String code;
    private final String message;

    HealthReportErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
