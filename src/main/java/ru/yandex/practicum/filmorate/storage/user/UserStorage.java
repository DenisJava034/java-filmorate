package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAllUsers();

    User createUsers(User user);

    User updateUsers(User newUser);

    User addToFriends(Long id, Long friendId);

    Collection<User> getFriendList(Long id);

    User deleteFromFriends(Long id, Long friendId);

    Collection<User> findCommonFriends(Long id, Long otherId);

    void checkId(Long id);
}
