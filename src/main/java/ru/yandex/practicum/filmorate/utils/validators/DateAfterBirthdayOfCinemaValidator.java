package ru.yandex.practicum.filmorate.utils.validators;

import ru.yandex.practicum.filmorate.utils.annotations.DateAfterBirthdayOfCinema;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Month;

public class DateAfterBirthdayOfCinemaValidator implements ConstraintValidator<DateAfterBirthdayOfCinema, LocalDate> {

    private static final LocalDate birthdayOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);
    private boolean allowNull;

    @Override
    public void initialize(DateAfterBirthdayOfCinema constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (allowNull) {
            return localDate == null || localDate.isAfter(birthdayOfCinema);
        } else {
            return localDate != null && localDate.isAfter(birthdayOfCinema);
        }
    }
}
