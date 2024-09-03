package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getPopular(Long count);

    Optional<Film> addLike(Long id, Long userId);

    Optional<Film> deleteLike(Long id, Long userId);

    List<Film> findAllFilms();

    Optional<Film> getById(Long id);

    Optional<Film> createFilms(Film film);

    Optional<Film> updateFilms(Film newFilm);

    void delete();

    void deleteById(Long id);


}
