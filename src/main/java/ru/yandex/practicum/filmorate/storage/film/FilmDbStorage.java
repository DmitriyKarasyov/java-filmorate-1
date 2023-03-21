package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "insert into film (name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        updateFilmGenre(film);
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", film.getId());
        if (userRows.next()) {
            jdbcTemplate.update("update film set name = ?, description = ?, release_date = ?, duration = ?, " +
                            "mpa_id = ? where film_id = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            updateFilmGenre(film);
            updateFilmLikes(film);
            return getFilmById(film.getId());
        } else {
            throw new NotFoundException("фильм");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "select * from film as f left outer join mpa as m on f.mpa_id = m.mpa_id where film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
            film.setGenres(getFilmGenresFromDb(film));
            film.setLikes(getFilmLikesFromDb(film));
            return film;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            throw new NotFoundException("фильм");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from film as f left outer join mpa as m on f.mpa_id = m.mpa_id";
        try {
            List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
            for (Film film : films) {
                film.setGenres(getFilmGenresFromDb(film));
                film.setLikes(getFilmLikesFromDb(film));
            }
            return films;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("фильм");
        }
    }

    public void updateFilmGenre(Film film) {
        jdbcTemplate.update("delete from film_genre where film_id = ?", film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("insert into film_genre (film_id, genre_id) values (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        long filmId = rs.getLong("film_id");
        String filmName = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Integer mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");

        return Film.builder()
                .id(filmId)
                .name(filmName)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(new Mpa(mpaId, mpaName))
                .build();
    }

    public Set<Genre> getFilmGenresFromDb(Film film) {
        String sql = "select f.genre_id, name from film_genre as f left outer join genre as g on " +
                "f.genre_id = g.genre_id where film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), film.getId());
        return new TreeSet<>(genres);
    }

    public Set<Long> getFilmLikesFromDb(Film film) {
        String sql = "select user_id from likes where film_id = ?";
        List<Long> likes = jdbcTemplate.queryForList(sql, Long.class, film.getId());

        return new TreeSet<>(likes);
    }

    public Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }

    public void updateFilmLikes(Film film) {
        jdbcTemplate.update("delete from likes where film_id = ?", film.getId());
        if (film.getLikes() != null) {
            for (Long userId : film.getLikes()) {
                jdbcTemplate.update("insert into likes (film_id, user_id) values (?, ?)", film.getId(), userId);
            }
        }
    }
}
