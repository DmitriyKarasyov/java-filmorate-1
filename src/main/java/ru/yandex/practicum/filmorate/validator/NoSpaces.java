package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = NoSpacesValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface NoSpaces {

    String message() default "Login should not contain spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
