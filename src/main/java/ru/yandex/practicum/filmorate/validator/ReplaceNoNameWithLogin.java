package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ReplaceNoNameWithLoginValidator.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface ReplaceNoNameWithLogin {

    String message() default "Name replaced with login";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
