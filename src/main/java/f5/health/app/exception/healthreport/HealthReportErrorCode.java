package f5.health.app.exception.healthreport;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum HealthReportErrorCode implements ErrorCode {

    NOT_FOUND_REPORT("기록된 리포트가 없습니다."),
    DUPLICATED_REPORT_SUBMIT("이미 기록된 리포트가 있습니다.");

    private final String code;
    private final String message;

    HealthReportErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
