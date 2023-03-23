package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {IsAfterValidator.class})
@Retention(RUNTIME)
@Target(FIELD)
public @interface IsAfter {

    String value();

    String message() default "Date can not be earlier than 1895-12-28";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
