package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count
    ) {
        return service.getPopularFilms(count);
    }
}
