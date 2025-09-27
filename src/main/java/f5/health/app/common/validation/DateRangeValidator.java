package f5.health.app.common.validation;

import f5.health.app.common.vo.DateRangeQuery;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * 조회 날짜 범위 검증기
 * @see DateRange
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, DateRangeQuery> {

    private int maxDays;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.maxDays = constraintAnnotation.maxDays();
    }

    @Override
    public boolean isValid(DateRangeQuery dateRange, ConstraintValidatorContext context) {
        LocalDate start = dateRange.start();
        LocalDate end = dateRange.end();
        if (start == null || end == null || start.isAfter(end)) {
            return false;
        }
        return DAYS.between(start, end) <= maxDays;
    }
}
