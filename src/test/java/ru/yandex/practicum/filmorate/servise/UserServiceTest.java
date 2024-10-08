package ru.yandex.practicum.filmorate.servise;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.builders.BuilderUser;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@JdbcTest
@Sql(scripts = {"/dataTest.sql"}, executionPhase = BEFORE_TEST_CLASS)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserService.class,
        DbUserStorage.class,
        UserRowMapper.class,
        BuilderUser.class})
class UserServiceTest {
    List<Long> idUsers = new ArrayList<>();
    private final UserService userService;


    @Test
    void create() {
        int i = userService.getAll().size();
        User user = User.builder()
                .name("name10")
                .login("login10")
                .email("a@aa11.ru")
                .birthday(LocalDate.now())
                .build();
        userService.create(user);
        assertEquals(userService.getAll().size(), i + 1);
    }

    @Test
    void createDoubleEmail() {
        User user = User.builder()
                .name("name10")
                .login("login10")
                .email("a@aa10.ru")
                .birthday(LocalDate.now())
                .friends(new HashSet<>())
                .build();
        User createUser = userService.create(user);
        user.setId(createUser.getId());
        Assertions.assertEquals(createUser, user);
        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class, () ->
                userService.create(user));
    }

    @Test
    void update() {
        long i = 3;
        User user = User.builder()
                .id(i)
                .name("newName")
                .login("login" + i)
                .email("a@aa" + i + ".ru")
                .birthday(LocalDate.now())
                .build();
        userService.update(user);
        assertEquals(userService.getById(i).getName(), "newName");

    }

    @Test
    void addFriendGetFrends() {
        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);
        User user = userService.getById(1);
        Set<Long> longList = user.getFriends();
        assertEquals(longList.size(), 2);
    }

    @Test
    void getCommonFriends() {
        userService.addFriend(1L, 2L);
        userService.addFriend(3L, 2L);
        assertEquals(userService.getCommonFriends(1L, 3L).size(), 1);
    }

    @Test
    void deleteFriend() {
        userService.addFriend(1L, 2L);
        userService.deleteFriend(1L, 2L);
        List<User> userList = userService.getFriends(1L);
        assertEquals(userList.size(), 0);
    }

    @Test
    void getAll() {
        assertEquals(userService.getAll().size(), 3);
    }
}