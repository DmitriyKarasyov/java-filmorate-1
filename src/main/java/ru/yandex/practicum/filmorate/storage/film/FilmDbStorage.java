package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

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
        String sql = "SELECT f.*, " +
                            "m.mpa_name, " +
                            "GROUP_CONCAT(g.genre_id) AS genre_id, " +
                            "GROUP_CONCAT(g.name) AS genre_name, " +
                            "GROUP_CONCAT(l.user_id) AS user_id " +
                "FROM film AS f " +
                "LEFT OUTER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT OUTER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT OUTER JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE f.film_id = ? " +
                "GROUP BY f.film_id";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            throw new NotFoundException("фильм");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, " +
                            "m.mpa_name, " +
                            "GROUP_CONCAT(g.genre_id) AS genre_id, " +
                            "GROUP_CONCAT(g.name) AS genre_name, " +
                            "GROUP_CONCAT(l.user_id) AS user_id " +
                "FROM film as f " +
                "LEFT OUTER JOIN mpa as m ON f.mpa_id = m.mpa_id " +
                "LEFT OUTER JOIN film_genre as fg ON f.film_id = fg.film_id " +
                "LEFT OUTER JOIN genre as g ON fg.genre_id = g.genre_id " +
                "LEFT OUTER JOIN likes as l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("фильм");
        }
    }

    public void updateFilmGenre(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
        if (film.getGenres() != null) {
            List<Genre> genres = new ArrayList<>(film.getGenres());
            String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, genres.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
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
        String genreId = rs.getString("genre_id");
        String genreName = rs.getString("genre_name");
        String filmLikes = rs.getString("user_id");


        return Film.builder()
                .id(filmId)
                .name(filmName)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(new Mpa(mpaId, mpaName))
                .genres(getFilmGenres(genreId, genreName))
                .likes(getFilmLikes(filmLikes))
                .build();
    }

    public Set<Genre> getFilmGenres(String genreId, String genreName) {
        Set<Genre> genres = new TreeSet<>();
        if (genreId != null && genreName != null) {
            String[] genreIdArray = genreId.split(",");
            String[] genreNameArray = genreName.split(",");
            for (int i = 0; i < genreIdArray.length; i++) {
                genres.add(new Genre(
                        Integer.parseInt(genreIdArray[i]),
                        genreNameArray[i]));
            }
        }
        return genres;
    }

    public Set<Long> getFilmLikes(String filmLikes) {
        Set<Long> likes = new TreeSet<>();
        if (filmLikes != null) {
            String[] filmLikesArray = filmLikes.split(",");
            for (String filmLike : filmLikesArray) {
                likes.add(Long.parseLong(filmLike));
            }
        }
        return likes;
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
