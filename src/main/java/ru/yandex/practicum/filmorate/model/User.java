package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NoSpaces;
import ru.yandex.practicum.filmorate.validator.ReplaceNoNameWithLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@ReplaceNoNameWithLogin
public class User {
    private Long id;

    private Set<Long> friends;

    @NotNull @NotBlank @Email
    private String email;

    @NotNull @NotBlank @NoSpaces
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.getEmail()) && Objects.equals(login, user.getLogin()) &&
                Objects.equals(name, user.getName()) && Objects.equals(birthday, user.getBirthday());
    }
}
