package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre implements Comparable<Genre> {

    private final Integer id;
    private final String name;

    @Override
    public int compareTo(Genre g) {
        return this.id - g.getId();
    }
}
