package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
    private final String parameter;
}
