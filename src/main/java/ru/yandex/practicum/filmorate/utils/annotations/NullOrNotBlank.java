package ru.yandex.practicum.filmorate.utils.annotations;

import ru.yandex.practicum.filmorate.utils.validators.NullOrNotBlankValidator;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Repeatable;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(NullOrNotBlank.List.class)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;

    @Target({FIELD})
    @Retention(RUNTIME)
    @interface List {
        NullOrNotBlank[] value();
    }
}
