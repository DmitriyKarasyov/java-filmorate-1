package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {NotLongerThanValidator.class})
@Retention(RUNTIME)
@Target(FIELD)
public @interface NotLongerThan {
    int value();

    String message() default "Description length should not be more than 200";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
