package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IsAfterValidator implements ConstraintValidator<IsAfter, LocalDate> {

    public String value;

    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isAfter(LocalDate.parse(value, formatter));
    }
}
