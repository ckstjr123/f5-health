package f5.health.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MenuSizeOverflowException extends IndexOutOfBoundsException {

    public MenuSizeOverflowException(int limit) {
        super("하루에 기록 가능한 메뉴 최대 개수는 " + limit + "개입니다.");
    }
}
