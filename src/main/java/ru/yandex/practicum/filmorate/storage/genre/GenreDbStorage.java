package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository

public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper mapper;
    private PreparedStatement stmt = null;

    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", mapper);
    }

    public Optional<Genre> getById(Long id) {
        String query = "SELECT * FROM genres WHERE genre_id = ?";
        final List<Genre> genres = jdbcTemplate.query(query, mapper, id);
        if (genres.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(genres.getFirst());
    }

    public Optional<Genre> create(Genre genre) {
        String query = "INSERT INTO genres (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            stmt = connection.prepareStatement(query, new String[]{"genre_id"});
            stmt.setString(3, genre.getName());
            return stmt;
        }, keyHolder);
        return getById(keyHolder.getKey().longValue());
    }

    public Optional<Genre> update(Genre genre) {
        String sqlQuery =
                "update genres SET name = ? WHERE genre_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                genre.getName(),
                genre.getId()
        );
        return getById(genre.getId());
    }

    public void delete() {
        jdbcTemplate.update("TRUNCATE TABLE genres");
    }

    public void deleteById(Long id) {
        String query = "DELETE FROM genres WHERE genre_id = ?";
        jdbcTemplate.update(query, id);
    }

}
