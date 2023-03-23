package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class InitializeIfNullValidator implements ConstraintValidator<InitializeIfNull, Set<Long>> {
    @Override
    public boolean isValid(Set<Long> set, ConstraintValidatorContext constraintValidatorContext) {
        if (set == null) {set = new HashSet<>();}
        return true;
    }
}
