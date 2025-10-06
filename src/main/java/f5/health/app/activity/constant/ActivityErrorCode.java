package f5.health.app.activity.constant;

import f5.health.app.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ActivityErrorCode implements ErrorCode {

    NOT_FOUND_ACTIVITY("기록된 활동 데이터가 없습니다."),
    DUPLICATED_ACTIVITY("이미 저장된 활동 기록이 있습니다. 수정을 통해 갱신해 주세요.");

    private final String code;
    private final String message;

    ActivityErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
