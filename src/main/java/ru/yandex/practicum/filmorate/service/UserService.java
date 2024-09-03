package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.builders.BuilderUser;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final BuilderUser builderUser;

    public List<User> getAll() {
        return userStorage.findAllUsers();
    }

    public User getById(long id) {
        User user = userStorage.getById(id).orElseThrow(() -> new NotFoundException("Нет user с заданным ID"));
        return builderUser.build(user);
    }

    public List<User> getFriends(Long id) {
        getById(id);
        return userStorage.getFriendList(id)
                .stream()
                .map(builderUser::build)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        return userStorage.findCommonFriends(userId, otherId)
                .stream()
                .map(builderUser::build)
                .collect(Collectors.toList());
    }

    public User create(User data) {
        validateAll(data);
        if (data.getName() == null || data.getName().isBlank()) {
            data.setName(data.getLogin());
        }
        log.debug("User создан: " + data);
        data = userStorage.createUsers(data).get();
        return getById(data.getId());
    }

    public User update(User data) {
        if (data.getId() == null) {
            throw new ValidationException("id должен быть указан");
        }
        final Optional<User> userOptional = userStorage.getById(data.getId());
        userOptional.orElseThrow(() -> new NotFoundException("Нет user c id:" + data.getId()));
        validateBirthday(data);
        log.debug("User c id: " + data.getId() + " обновлен");
        return userStorage.updateUsers(data).get();
    }


    public void delete() {
        userStorage.delete();
    }


    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
        userStorage.addToFriends(userId, friendId);
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Нет user с ID: " + userId));
        userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException("Нет friends с ID: " + friendId));
        userStorage.deleteFromFriends(userId, friendId);
        return builderUser.build(user);
    }

    public void validateAll(final User newUser) {
        validateEmail(newUser);
        validateBirthday(newUser);
    }

    public void validateEmail(final User newUser) {
        if (userStorage.findEmail(newUser)) {
            final String s = "Этот имейл уже используется";
            log.info("Вызвано исключение: " + s + " Пришло: " + newUser.getEmail());
            throw new DuplicatedDataException(s);
        }
    }

    public void validateBirthday(final User newUser) {
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            final String s = "Дата рождения не может быть в будущем";
            log.info("Вызвано исключение: " + s + " Пришло: " + newUser.getBirthday());
            throw new ValidationException(s);
        }
    }

}