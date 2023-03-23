package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReplaceNoNameWithLoginValidator implements ConstraintValidator<ReplaceNoNameWithLogin, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        if (user.getName() == null || "".equals(user.getName())) {
            user.setName(user.getLogin());
        }
        return true;
    }
}