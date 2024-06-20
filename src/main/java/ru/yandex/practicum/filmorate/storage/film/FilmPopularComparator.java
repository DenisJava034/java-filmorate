package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmPopularComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        if (film2.getLikes() == null) {
            if (film1.getLikes() == null) {
                return 0;
            }
            return -1;
        }
        if (film1.getLikes() == null) {
            return 1;
        }
        return film2.getLikes().size() - film1.getLikes().size();
    }
}

