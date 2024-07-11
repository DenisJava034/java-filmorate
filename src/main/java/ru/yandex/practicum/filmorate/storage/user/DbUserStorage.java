package ru.yandex.practicum.filmorate.storage.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Repository
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper mapper;
    private PreparedStatement stmt = null;

    @Override
    public List<User> findAllUsers() {
        List<User> list = jdbcTemplate.query("SELECT * FROM USERS", mapper);
        return list;
    }

    @Override
    public Optional<User> getById(Long id) {
        String query = "SELECT * FROM USERS WHERE user_id = ?";
        final List<User> users = jdbcTemplate.query(query, mapper, id);
        if (users.size() == 0) {
            throw new NotFoundException("user id = " + id);
        }
        User user = users.getFirst();
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> createUsers(User user) {
        String query = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            stmt = connection.prepareStatement(query, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<User> updateUsers(User user) {
        String sqlQuery =
                "update users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return getById(user.getId());
    }

    @Override
    public List<User> getFriendList(Long id) {
        String query = "SELECT * " +
                "FROM users " +
                "WHERE user_id " +
                "IN (SELECT f.friend_id  " +
                "FROM USERS u " +
                "LEFT JOIN FRIENDS f " +
                "ON u.user_id = f.user_id " +
                "WHERE u.user_id = ?)";
        List<User> users = jdbcTemplate.query(query, mapper, id);
        return users;
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long otherId) {
        String query = "SELECT * FROM USERS " +
                "WHERE user_id IN (" +
                "SELECT friend_id  FROM FRIENDS " +
                "WHERE USER_ID =? " +
                "AND friend_id IN(SELECT friend_id FROM friends WHERE user_id = ?));";
        return jdbcTemplate.query(query, mapper, userId, otherId);
    }

    @Override
    public boolean findEmail(User newUser) {
        List<User> userList = jdbcTemplate.query("Select * from users WHERE email = ?", mapper, newUser.getEmail());
        return !userList.isEmpty();
    }

    @Override
    public Optional<User> addToFriends(Long userId, Long friendId) {
        String query = "MERGE INTO friends VALUES (?, ?)";
        jdbcTemplate.update(query, userId, friendId);
        log.debug("Друг добавлен для user: " + userId);
        return Optional.empty();
    }

    @Override
    public User deleteFromFriends(Long userId, Long friendId) {
        String query = "DELETE FROM FRIENDS WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(query, userId, friendId);
        return null;
    }

    @Override
    public void delete() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Override
    public void deleteById(Long id) {
        String query = "delete from users WHERE user_id = ?";
        jdbcTemplate.update(query, id);
    }

}