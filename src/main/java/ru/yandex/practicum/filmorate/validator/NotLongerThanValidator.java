package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotLongerThanValidator implements ConstraintValidator<NotLongerThan, String> {

    public int value;

    @Override
    public void initialize(NotLongerThan constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() <= value;
    }
}
