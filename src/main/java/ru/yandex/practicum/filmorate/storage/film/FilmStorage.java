package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    Film createFilms(Film film);

    Film updateFilms(Film newFilm);

    Film addLike(Long id, Long userId);

    Film deleteLike(Long id, Long userId);

    Collection<Film> getPopular(Long count);


}
