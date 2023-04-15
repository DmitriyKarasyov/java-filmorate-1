package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = InitializeIfNullValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface InitializeIfNull {

    String message() default "Can not initialize the field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
