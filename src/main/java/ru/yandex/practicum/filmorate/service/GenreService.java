package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.List;

@Service
public class GenreService {

    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(Integer id) {
        return genreDao.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}
