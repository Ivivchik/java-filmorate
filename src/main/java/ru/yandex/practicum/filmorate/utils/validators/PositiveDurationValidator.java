package ru.yandex.practicum.filmorate.utils.validators;

import ru.yandex.practicum.filmorate.utils.annotations.PositiveDuration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {
    private boolean allowNull;

    @Override
    public void initialize(PositiveDuration constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        if (allowNull) {
            return duration == null || duration.isPositive();
        } else {
            return duration != null && duration.isPositive();
        }
    }
}
