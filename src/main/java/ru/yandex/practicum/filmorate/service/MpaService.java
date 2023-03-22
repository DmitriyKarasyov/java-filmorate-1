package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.List;

@Service
public class MpaService {

    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }
}
