package ru.yandex.practicum.filmorate.utils.annotations;

import ru.yandex.practicum.filmorate.utils.validators.DateAfterBirthdayOfCinemaValidator;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Repeatable;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(DateAfterBirthdayOfCinema.List.class)
@Constraint(validatedBy = DateAfterBirthdayOfCinemaValidator.class)
public @interface DateAfterBirthdayOfCinema {
    String message() default "{CapitalLetter.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;

    @Target({FIELD})
    @Retention(RUNTIME)
    @interface List {
        DateAfterBirthdayOfCinema[] value();
    }
}
