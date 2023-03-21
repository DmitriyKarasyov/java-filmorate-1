package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "select * from genre where genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("жанр");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "select * from genre";
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}
