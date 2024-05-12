package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;
    private static final LocalDate dateOfFirstFilm = LocalDate.of(1895, 12, 28);

    public Collection<Film> findAll() {
        return storage.findAllFilms();
    }

    public Film create(Film film) {
        checkReleaseDate(film);
        return storage.createFilms(film);
    }

    public Film update(Film newFilm) {
        checkReleaseDate(newFilm);
        return storage.updateFilms(newFilm);
    }

    public Film addLike(Long id, Long userId) {
        return storage.addLike(id, userId);
    }

    public Film deleteLike(Long id, Long userId) {
        return storage.deleteLike(id, userId);
    }

    public Collection<Film> getPopular(Long count) {
        return storage.getPopular(count);
    }

    private void checkReleaseDate(Film newFilm) {
        if (newFilm.getReleaseDate().isBefore(dateOfFirstFilm)) {
            log.error("Incorrect movie release date - {}", newFilm.getReleaseDate());
            throw new ValidationException("Дата фильма не может быть раньше 28.12.1895");
        }
    }

}
