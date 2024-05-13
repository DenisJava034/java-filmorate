package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User createUsers(User user) {
        user.setId(getNextId());
        log.debug("The user is assigned id {}", user.getId());
        users.put(user.getId(), user);
        log.info("New user created");
        return user;
    }

    public User updateUsers(User newUser) {
        if (newUser.getId() == null) {
            log.error("Id not specified");
            throw new NotFoundException("Id должен быть указан");
        }
        checkId(newUser.getId());

        log.debug("user with id {} found", newUser.getId());
        users.put(newUser.getId(), newUser);
        log.info("Update user");
        return newUser;
    }

    @Override
    public User addToFriends(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);

        Set<Long> friends1 = users.get(id).getFriends();
        if (friends1 == null) {
            friends1 = new HashSet<>();
        }

        Set<Long> friends2 = users.get(friendId).getFriends();
        if (friends2 == null) {
            friends2 = new HashSet<>();
        }

        friends1.add(friendId);
        friends2.add(id);

        users.get(id).setFriends(friends1);
        users.get(friendId).setFriends(friends2);
        log.info("Пользователь с id = {} добавил в друзья пользователя с id = {}", id, friendId);
        return users.get(id);
    }

    @Override
    public Collection<User> getFriendList(Long id) {
        checkId(id);
        Set<Long> setUser = users.get(id).getFriends();
        if (setUser == null) {
            setUser = new HashSet<>();
        }
        LinkedList<User> userList = new LinkedList<>();
        for (Long idFriend : setUser) {
            userList.add(users.get(idFriend));
        }
        log.info("Получение списка друзей пользователя с id = {}", id);
        return userList;
    }

    @Override
    public User deleteFromFriends(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);

        Set<Long> friends1 = users.get(id).getFriends();

        if (friends1 != null) {
            friends1.remove(friendId);
        }

        Set<Long> friends2 = users.get(friendId).getFriends();

        if (friends2 != null) {
            friends2.remove(id);
        }
        users.get(id).setFriends(friends1);
        users.get(friendId).setFriends(friends2);
        log.info("Пользователь с id = {} удалил из друзей пользователя с id = {}", friendId, id);
        return users.get(id);
    }

    @Override
    public Collection<User> findCommonFriends(Long id, Long otherId) {
        LinkedList<User> friendCommon = new LinkedList<>();

        Set<Long> friends1 = users.get(id).getFriends();
        Set<Long> friends2 = users.get(otherId).getFriends();

        log.info("Получение общих друзей пользователя с id = {} и пользователя с id = {}", id, otherId);
        friends1.retainAll(friends2); // сохранение одинаковых id

        for (Long idFriend : friends1) {
            friendCommon.add(users.get(idFriend));
        }

        return friendCommon;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void checkId(Long id) {
        if (!users.containsKey(id)) {
            log.error("user with id {} is not equipped", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
