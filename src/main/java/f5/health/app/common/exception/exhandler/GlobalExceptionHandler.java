package f5.health.app.common.exception.exhandler;

import f5.health.app.common.exception.AccessDeniedException;
import f5.health.app.auth.exception.AuthenticationException;
import f5.health.app.common.exception.BadRequestException;
import f5.health.app.common.exception.ConflictException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.common.exception.exhandler.response.FieldError;
import f5.health.app.common.exception.exhandler.response.ExceptionResult;
import f5.health.app.common.exception.exhandler.response.ErrorsResult;
import f5.health.app.common.exception.exhandler.response.GlobalError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorsResult methodArgumentNotValidExHandler(MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidExHandler", ex);

        List<GlobalError> globalErrors = ex.getBindingResult()
                .getGlobalErrors().stream()
                .map(objectError -> new GlobalError(objectError.getCode(), objectError.getDefaultMessage()))
                .toList();
        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return ErrorsResult.of(globalErrors, fieldErrors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionResult notFoundExHandler(NotFoundException ex) {
        log.warn("NotFoundExHandler", ex);
        return ExceptionResult.of(ex.getErrorCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionResult badRequestExHandler(BadRequestException ex) {
        log.warn("BadRequestExHandler", ex);
        return ExceptionResult.of(ex.getErrorCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ExceptionResult authenticationExHandler(AuthenticationException ex) {
        log.warn("AuthenticationExHandler", ex);
        return ExceptionResult.from(ex.getErrorCode());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ExceptionResult duplicateExHandler(ConflictException ex) {
        log.warn("DuplicateExHandler", ex);
        return ExceptionResult.from(ex.getErrorCode());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionResult accessDeniedExHandler(AccessDeniedException ex) {
        log.warn("AccessDeniedExHandler", ex);
        return ExceptionResult.of(ex.getErrorCode(), ex.getMessage());
    }



}
