package ru.yandex.practicum.filmorate.builders;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@AllArgsConstructor
public class BuilderUser {
    private final JdbcTemplate jdbcTemplate;

    private Set<Long> getUserFriends(Long id) {
        String query = "SELECT * FROM friends WHERE user_id=?";
        Set<Long> set = new HashSet<>();
        List<Friend> friends = jdbcTemplate.query(query, BuilderUser::makeFriend, id);
        for (Friend f : friends) {
            set.add(f.getSecondId());
        }
        return set;
    }

    private static Friend makeFriend(ResultSet rs, int rowNum) throws SQLException {
        return new Friend(
                rs.getLong("user_id"),
                rs.getLong("friend_id"));
    }

    public User build(User user) {
        long id = user.getId();
        user.setFriends(getUserFriends(id));
        return user;
    }
}
