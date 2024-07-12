package ru.yandex.practicum.filmorate.storage.genre.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreResultSetExtractor implements ResultSetExtractor<List<Genre>> {
    @Override
    public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            Genre g = new Genre();
            g.setId(rs.getLong("id"));
            g.setName(rs.getString("name"));
            genres.add(g);
        }

        return genres;
    }
}
