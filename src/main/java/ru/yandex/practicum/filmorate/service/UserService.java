package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;

    public Collection<User> findAll() {
        return storage.findAllUsers();
    }

    public User create(User user) {
        checkLogin(user);
        checkName(user);
        return storage.createUsers(user);
    }

    public User update(User newUser) {
        checkLogin(newUser);
        checkName(newUser);
        return storage.updateUsers(newUser);
    }

    public User addToFriends(Long id, Long friendId) {
        return storage.addToFriends(id, friendId);
    }

    public Collection<User> getFriendList(Long id) {
        return storage.getFriendList(id);
    }

    public User deleteFromFriends(Long id, Long friendId) {
        return storage.deleteFromFriends(id, friendId);
    }

    public Collection<User> findCommonFriends(Long id, Long otherId) {
        return storage.findCommonFriends(id, otherId);
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



