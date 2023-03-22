package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();

    private static long id = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(id);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(id++, film);
        log.info("Фильм {} успешно добавлен.", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Фильм {} успешно обновлён.", film.getName());
        } else {
            throw new NotFoundException("фильм");
        }
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("фильм");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film removeFilmById(Long id) {
        if (films.containsKey(id)) {
            films.remove(id);
            return films.get(id);
        } else {
            throw new NotFoundException("фильм");
        }
    }
}
