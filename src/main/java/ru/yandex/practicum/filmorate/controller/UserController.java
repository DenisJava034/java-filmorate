package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUsers(@Valid @RequestBody User user) {
        checkLogin(user);
        checkName(user);

        user.setId(getNextId());
        log.debug("The user is assigned id {}", user.getId());
        users.put(user.getId(), user);
        log.info("New user created");
        return user;
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Id not specified");
            throw new ValidationException("Id должен быть указан");
        }

        if (!users.containsKey(newUser.getId())) {
            log.error("user with id {} is not equipped", newUser.getId());
            throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        checkLogin(newUser);
        log.debug("user with id {} found", newUser.getId());
        checkName(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Update user");
        return newUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkLogin(User newUser) {
        if (newUser.getLogin().contains(" ")) {
            log.error("login contains spaces - {}", newUser.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }

    private void checkName(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            log.info("Username not specified");
            newUser.setName(newUser.getLogin());
        }
    }
}
