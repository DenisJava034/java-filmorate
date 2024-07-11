package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

@RequiredArgsConstructor
@Repository
public class ListGenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addGenreToListGenges(Film film, Long id) {
        String query = "MERGE INTO GENRE_FILM " +
                "(FILM_ID, GENRE_ID) " +
                "VALUES (?, ?)";
        for (Genre g : film.getGenres()) {
            jdbcTemplate.update(query, id, g.getId());
        }

    }
}
