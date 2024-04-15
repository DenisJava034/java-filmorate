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
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    static FilmController filmController;
    static UserController userController;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        userController = new UserController();
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
                ValidationException.class,
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
                ValidationException.class,
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
                ValidationException.class,
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
                ValidationException.class,
                () -> userController.updateUsers(user)
        );
        assertEquals("Пользователь с id = " + user.getId() + " не найден", exception.getMessage());
    }

}
