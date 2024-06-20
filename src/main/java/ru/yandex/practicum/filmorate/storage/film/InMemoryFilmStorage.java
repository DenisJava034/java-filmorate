package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();


    private final UserStorage userStorage;


    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilms(Film film) {
        film.setId(getNextId());
        log.debug("The film is assigned id {}", film.getId());
        films.put(film.getId(), film);
        log.info("New film created");
        return film;
    }

    @Override
    public Film updateFilms(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id not specified");
            throw new NotFoundException("Id должен быть указан");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.error("Film with id {} is not equipped", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Update film");
        return newFilm;
    }

    @Override
    public Film addLike(Long id, Long userId) {
        checkFilmId(id);
        userStorage.checkId(userId);

        Set<Long> likes = films.get(id).getLikes();
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(userId);
        films.get(id).setLikes(likes);
        log.info("Пользователь с id = {} поставил лайк фильму id = {}", userId, id);

        return films.get(id);
    }

    @Override
    public Film deleteLike(Long id, Long userId) {
        checkFilmId(id);
        userStorage.checkId(userId);
        Set<Long> likes = films.get(id).getLikes();
        likes.remove(userId);
        films.get(id).setLikes(likes);
        log.info("Пользователь с id = {} удалил лайк фильму id = {}", userId, id);

        return films.get(id);
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        if (count < 0) {
            throw new ValidationException("Count должен быть больше 0");
        }
        log.info("Вывод списка {} самых популярных фильмов", count);
        List<Film> film = films.values()
                .stream()
                .sorted(new FilmPopularComparator())
                .limit(count).collect(Collectors.toList());
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkFilmId(Long id) {
        if (!films.containsKey(id)) {
            log.error("film with id {} is not equipped", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }
}
