package ru.yandex.practicum.filmorate.storage.user.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> users = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            User user = users.get(id);
            if (user == null) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setLogin(rs.getString("login"));
                u.setEmail(rs.getString("email"));
                u.setBirthday(rs.getObject("birthday", LocalDate.class));
                long likeId = rs.getLong("friend_id");
                if (likeId != 0) {
                    u.getFriends().add(likeId);
                }
                users.put(id, u);
            } else {
                user.getFriends().add(rs.getLong("friend_id"));
            }
        }


        return new ArrayList<>(users.values());
    }
}
