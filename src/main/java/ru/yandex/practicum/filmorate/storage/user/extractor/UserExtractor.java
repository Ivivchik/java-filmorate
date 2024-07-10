package ru.yandex.practicum.filmorate.storage.user.extractor;

import ru.yandex.practicum.filmorate.model.User;

import org.springframework.stereotype.Component;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.SQLException;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> userToUser = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            User user = userToUser.get(id);
            if (user == null) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getObject("birthday", LocalDate.class));
                user.getFriends().add(rs.getLong("friend_id"));
                userToUser.put(id, user);
            } else {
                user.getFriends().add(rs.getLong("friend_id"));
            }
        }

        return new ArrayList<>(userToUser.values());
    }
}
