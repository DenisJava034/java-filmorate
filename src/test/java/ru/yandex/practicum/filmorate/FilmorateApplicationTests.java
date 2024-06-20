package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    static FilmController filmController;
    static UserController userController;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        final UserStorage userStorage = new InMemoryUserStorage();
        final FilmStorage storageFilm = new InMemoryFilmStorage(userStorage);
        FilmService filmService = new FilmService(storageFilm);
        filmController = new FilmController(filmService);


        UserService serviceUser = new UserService(userStorage);
        userController = new UserController(serviceUser);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void wrongFilmReleaseDate() {
        final Film film = Film.builder()
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(90)
                .build();

        Exception exception = assertThrows(
                ValidationException.class,
                () -> filmController.createFilms(film)
        );
        assertEquals("Дата фильма не может быть раньше 28.12.1895", exception.getMessage());
    }

    @Test
    void emptyTitleFilm() {
        final Film film = Film.builder()  // название пустое
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(90)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        final Film film2 = Film.builder()  // нет названия
                .name(null)
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(90)
                .build();

        violations = validator.validate(film2);
        assertFalse(violations.isEmpty());
    }

    @Test
    void descriptionMoreThan200Characters() {
        String descrip = "1".repeat(201);
        final Film film = Film.builder()
                .name("Фильм")
                .description(descrip)
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(90)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmLengthIsANegativeNumber() {

        final Film film = Film.builder()
                .name("Фильм")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(-90)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userCreateFailEmail() {

        final User user = User.builder()
                .login("dolore")
                .name("name")
                .email("mail.ru") // Не верный email
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        final User user2 = User.builder()
                .login("dolore")
                .name("name")
                .email("") // Пустой email
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        violations = validator.validate(user2);
        assertFalse(violations.isEmpty());
    }

    @Test
    void emptyLogin() {
        final User user = User.builder()
                .login("")
                .name("name")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void loginWithSpaces() {
        final User user = User.builder()
                .login("dolore ullamco")
                .name("name")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        Exception exception = assertThrows(
                ValidationException.class,
                () -> userController.createUsers(user)
        );
        assertEquals("Логин не должен содержать пробелы", exception.getMessage());
    }

    @Test
    void emptyName() {
        final User user = User.builder()
                .login("dolore")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();
        userController.createUsers(user);

        assertEquals(user.getName(), "dolore", "Имя не равен логину");
    }

    @Test
    void userCreateFailBirthday() {

        final User user = User.builder()
                .login("dolore")
                .name("name")
                .email("test@mail.ru")
                .birthday(LocalDate.of(2500, 8, 2))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void updateFilmNoId() {
        final Film film = Film.builder()
                .id(null)
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1900, 12, 27))
                .duration(90)
                .build();

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> filmController.updateFilms(film)
        );
        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    void updateUserNoId() {
        final User user = User.builder()
                .id(null)
                .login("dolore")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> userController.updateUsers(user)
        );
        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    void updateFilmWrongId() {
        final Film film = Film.builder()
                .id(9L)
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1900, 12, 27))
                .duration(90)
                .build();

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> filmController.updateFilms(film)
        );
        assertEquals("Фильм с id = " + film.getId() + " не найден", exception.getMessage());
    }

    @Test
    void updateUserWrongId() {
        final User user = User.builder()
                .id(55L)
                .login("dolore")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> userController.updateUsers(user)
        );
        assertEquals("Пользователь с id = " + user.getId() + " не найден", exception.getMessage());
    }

    @Test
    void getPopularFilm() {

        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();

        final User user3 = User.builder()
                .login("Александр")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1986, 7, 12))
                .build();

        Long idUser1 = userController.createUsers(user1).getId();
        Long idUser2 = userController.createUsers(user2).getId();
        Long idUser3 = userController.createUsers(user3).getId();

        final Film film1 = Film.builder()  // название пустое
                .name("Кино1")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();

        final Film film2 = Film.builder()  // название пустое
                .name("Кино2")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();

        final Film film3 = Film.builder()  // название пустое
                .name("Кино3")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();

        Long idFilm1 = filmController.createFilms(film1).getId();
        Long idFilm2 = filmController.createFilms(film2).getId();
        Long idFilm3 = filmController.createFilms(film3).getId();

        //ставим лайки
        filmController.addLikeTheMovie(idFilm3, idUser1);
        filmController.addLikeTheMovie(idFilm3, idUser2);
        filmController.addLikeTheMovie(idFilm3, idUser3);

        filmController.addLikeTheMovie(idFilm1, idUser1);
        filmController.addLikeTheMovie(idFilm1, idUser3);

        Collection<Film> listPopularFilms = filmController.getPopular(2L);

        assertEquals(listPopularFilms.size(), 2, "Список фильмов не равен 2");
    }

    // test friend
    @Test
    void addFriend() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();

        Long idUser1 = userController.createUsers(user1).getId();
        Long idUser2 = userController.createUsers(user2).getId();

        userController.addAsFriend(idUser1, idUser2);

        assertTrue(userController.getFriendList(user1.getId()).contains(user2));
        assertTrue(userController.getFriendList(user2.getId()).contains(user1));
    }

    @Test
    void addUnknownFriend() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();

        Long idUser1 = userController.createUsers(user1).getId();


        Exception exception = assertThrows(
                NotFoundException.class,
                () -> userController.addAsFriend(idUser1, 4L)
        );

        assertFalse(userController.getFriendList(user1.getId()).contains(user2));
        assertEquals("Пользователь с id = " + 4 + " не найден", exception.getMessage());
    }

    @Test
    void getFriendsList() {

        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();

        final User user3 = User.builder()
                .login("Александр")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1986, 7, 12))
                .build();

        Long idUser1 = userController.createUsers(user1).getId();
        Long idUser2 = userController.createUsers(user2).getId();
        Long idUser3 = userController.createUsers(user3).getId();

        userController.addAsFriend(idUser1, idUser2);
        userController.addAsFriend(idUser1, idUser3);

        Collection<User> listUser = new ArrayList<>();
        listUser.add(user2);
        listUser.add(user3);

        Collection<User> listUserFriends = userController.getFriendList(idUser1);

        assertEquals(listUserFriends.size(), 2, "Список друзей не равен 2");
        assertTrue(listUserFriends.equals(listUser), "Список друзей не совпадает");

    }

    @Test
    void getFriendsListUnknownUser() {

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> userController.getFriendList(1L)
        );

        assertEquals("Пользователь с id = " + 1 + " не найден", exception.getMessage());
    }

    @Test
    void deleteFriend() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();


        Long idUser1 = userController.createUsers(user1).getId();
        Long idUser2 = userController.createUsers(user2).getId();

        userController.addAsFriend(idUser1, idUser2);

        Collection<User> listUser = new ArrayList<>();
        listUser.add(user2);

        Collection<User> listUserFriends = userController.getFriendList(idUser1);

        assertEquals(listUserFriends.size(), 1, "Список друзей не равен 1");
        assertTrue(listUserFriends.equals(listUser), "Список друзей не совпадает");

        userController.deleteFromFriends(idUser1, idUser2);

        assertEquals(userController.getFriendList(idUser1).size(), 0, "Список друзей не равен 0");
        assertFalse(userController.getFriendList(idUser1).contains(user2), "Список друзей не совпадает");

    }

    @Test
    void deleteFriendsUnknownUser() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();

        Long idUser1 = userController.createUsers(user1).getId();
        Long idUser2 = userController.createUsers(user2).getId();

        userController.addAsFriend(idUser1, idUser2);

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> userController.deleteFromFriends(idUser1, 5L)
        );

        assertEquals("Пользователь с id = " + 5 + " не найден", exception.getMessage());
    }

    @Test
    void findCommonFriends() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();

        final User user2 = User.builder()
                .login("Федор")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1982, 7, 12))
                .build();

        final User user3 = User.builder()
                .login("Александр")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1986, 7, 12))
                .build();

        Long idUser1 = userController.createUsers(user1).getId();
        Long idUser2 = userController.createUsers(user2).getId();
        Long idUser3 = userController.createUsers(user3).getId();

        userController.addAsFriend(idUser1, idUser2);
        userController.addAsFriend(idUser1, idUser3);
        userController.addAsFriend(idUser2, idUser3);

        Collection<User> listUserFriends1 = userController.getListOfCommonFriends(idUser1, idUser2);

        assertTrue(listUserFriends1.contains(user3), "Общие друзья не совпадает");
    }


    // test like
    @Test
    void addLike() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();
        Long idUser1 = userController.createUsers(user1).getId();

        final Film film = Film.builder()  // название пустое
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();
        Long idFilm = filmController.createFilms(film).getId();
        filmController.addLikeTheMovie(idUser1, idFilm);
        ArrayList<Film> films = new ArrayList<>(filmController.findAllFilms());

        assertTrue(films.get(0).getLikes().contains(idUser1));
    }

    @Test
    void addLikeUnknownFilm() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();
        Long idUser1 = userController.createUsers(user1).getId();

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> filmController.addLikeTheMovie(5L, idUser1)
        );

        assertEquals("Фильм с id = " + 5 + " не найден", exception.getMessage());

    }

    @Test
    void addLikeUnknownUser() {
        final Film film = Film.builder()  // название пустое
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();
        Long idFilm = filmController.createFilms(film).getId();

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> filmController.addLikeTheMovie(idFilm, 7L)
        );

        assertEquals("Пользователь с id = " + 7 + " не найден", exception.getMessage());

    }

    @Test
    void deleteLike() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();
        Long idUser1 = userController.createUsers(user1).getId();

        final Film film = Film.builder()  // название пустое
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();
        Long idFilm = filmController.createFilms(film).getId();
        filmController.addLikeTheMovie(idUser1, idFilm);
        ArrayList<Film> films = new ArrayList<>(filmController.findAllFilms());

        assertTrue(films.get(0).getLikes().contains(idUser1));

        filmController.removeLikeFilm(idFilm, idUser1);
        films = new ArrayList<>(filmController.findAllFilms());

        assertFalse(films.get(0).getLikes().contains(idUser1));
    }

    @Test
    void deleteLikeUnknownFilm() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();
        Long idUser1 = userController.createUsers(user1).getId();

        final Film film = Film.builder()  // название пустое
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();
        Long idFilm = filmController.createFilms(film).getId();
        filmController.addLikeTheMovie(idUser1, idFilm);
        ArrayList<Film> films = new ArrayList<>(filmController.findAllFilms());

        assertTrue(films.get(0).getLikes().contains(idUser1));

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> filmController.addLikeTheMovie(5L, idUser1)
        );

        assertEquals("Фильм с id = " + 5 + " не найден", exception.getMessage());
    }

    @Test
    void deleteLikeUnknownUser() {
        final User user1 = User.builder()
                .login("Иван")
                .name("")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1980, 8, 2))
                .build();
        Long idUser1 = userController.createUsers(user1).getId();

        final Film film = Film.builder()  // название пустое
                .name("Кино")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 12, 29))
                .duration(90)
                .build();
        Long idFilm = filmController.createFilms(film).getId();
        filmController.addLikeTheMovie(idUser1, idFilm);
        ArrayList<Film> films = new ArrayList<>(filmController.findAllFilms());

        assertTrue(films.get(0).getLikes().contains(idUser1));

        Exception exception = assertThrows(
                NotFoundException.class,
                () -> filmController.addLikeTheMovie(idFilm, 8L)
        );

        assertEquals("Пользователь с id = " + 8 + " не найден", exception.getMessage());
    }
}
