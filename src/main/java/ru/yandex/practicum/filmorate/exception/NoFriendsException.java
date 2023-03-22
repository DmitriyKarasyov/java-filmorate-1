package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

@Data
public class NoFriendsException extends RuntimeException {
    private final Long parameter;
}
