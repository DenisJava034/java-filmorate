package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
@Repository
public class ListGenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addGenreToListGenges(Film film, Long id) {
        String query = "MERGE INTO GENRE_FILM " +
                "(FILM_ID, GENRE_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.batchUpdate(query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre g = film.getGenres().get(i);
                        ps.setLong(1, id);
                        ps.setLong(2, g.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                }
        );
    }
}
