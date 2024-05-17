package ru.yandex.practicum.filmorate.utils.annotations;

import ru.yandex.practicum.filmorate.utils.validators.CustomEmailValidator;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Email;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Repeatable;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Email
@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(CustomEmail.List.class)
@Constraint(validatedBy = CustomEmailValidator.class)
public @interface CustomEmail {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;

    @Target({FIELD})
    @Retention(RUNTIME)
    @interface List {
        CustomEmail[] value();
    }
}
