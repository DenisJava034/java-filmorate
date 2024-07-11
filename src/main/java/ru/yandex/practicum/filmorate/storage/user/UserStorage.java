package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getFriendList(Long id);

    List<User> findCommonFriends(Long userId, Long otherId);

    boolean findEmail(final User newUser);

    Optional<User> addToFriends(Long id, Long friendId);

    User deleteFromFriends(Long id, Long friendId);

    List<User> findAllUsers();

    Optional<User> getById(Long id);

    Optional<User> createUsers(User user);

    Optional<User> updateUsers(User newUser);

    void delete();

    void deleteById(Long id);


}
