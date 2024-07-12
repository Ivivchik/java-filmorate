package ru.yandex.practicum.filmorate.storage.rating.mapper;

import ru.yandex.practicum.filmorate.model.Rating;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<Rating> {
    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rating r = new Rating();
        r.setId(rs.getLong("id"));
        r.setName(rs.getString("name"));

        return r;
    }
}
