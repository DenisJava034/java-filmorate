package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate dateOfFirstFilm = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film) {
        checkReleaseDate(film);

        film.setId(getNextId());
        log.debug("The film is assigned id {}", film.getId());
        films.put(film.getId(), film);
        log.info("New film created");
        return film;
    }

    @PutMapping
    public Film updateFilms(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id not specified");
            throw new ValidationException("Id должен быть указан");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.error("Film with id {} is not equipped", newFilm.getId());
            throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        checkReleaseDate(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Update film");
        return newFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkReleaseDate(Film newFilm) {
        if (newFilm.getReleaseDate().isBefore(dateOfFirstFilm)) {
            log.error("Incorrect movie release date - {}", newFilm.getReleaseDate());
            throw new ValidationException("Дата фильма не может быть раньше 28.12.1895");
        }
    }
}
