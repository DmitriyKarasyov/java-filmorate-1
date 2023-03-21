package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {

    public Mpa getMpaById(Integer id);

    public List<Mpa> getAllMpa();
}
