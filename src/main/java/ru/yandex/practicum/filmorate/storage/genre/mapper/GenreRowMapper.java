package ru.yandex.practicum.filmorate.storage.genre.mapper;

import ru.yandex.practicum.filmorate.model.Genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre g = new Genre();
        g.setId(rs.getLong("id"));
        g.setName(rs.getString("name"));

        return g;
    }
}
