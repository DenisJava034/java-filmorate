package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mapper;
    private PreparedStatement stmt = null;

    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM film_rating", mapper);
    }

    public Optional<Mpa> getById(Long id) {
        String query = "SELECT * FROM film_rating WHERE film_rating_id = ?";
        final List<Mpa> listMpa = jdbcTemplate.query(query, mapper, id);
        if (listMpa.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(listMpa.getFirst());
    }

    public Optional<Mpa> create(Mpa data) {
        String query = "INSERT INTO film_rating (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            stmt = connection.prepareStatement(query, new String[]{"film_rating_id"});
            stmt.setString(3, data.getName());
            return stmt;
        }, keyHolder);
        return getById(keyHolder.getKey().longValue());
    }

    public Optional<Mpa> update(Mpa data) {
        String sqlQuery =
                "update film_rating SET name = ? WHERE film_rating_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                data.getName(),
                data.getId()
        );
        return getById(data.getId());
    }

    public void delete() {
        jdbcTemplate.update("TRUNCATE TABLE film_rating");
    }

    public void deleteById(long id) {
        String query = "DELETE FROM film_rating WHERE film_rating_id = ?";
        jdbcTemplate.update(query, id);
    }

}
