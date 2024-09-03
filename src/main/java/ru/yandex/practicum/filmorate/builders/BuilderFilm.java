package ru.yandex.practicum.filmorate.builders;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmJoinGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class BuilderFilm {
    private final JdbcTemplate jdbcTemplate;

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getLong("genre_id"),
                rs.getString("name"));
    }

   private static FilmJoinGenre makeFilmJoinGenre(ResultSet rs, int rowNum) throws SQLException {
        FilmJoinGenre filmJoinGenre = new FilmJoinGenre(
                rs.getLong("film_id"),
                rs.getLong("genre_id"),
                rs.getString("name")
        );
        return filmJoinGenre;
    }

    static Like makeLike(ResultSet rs, int rowNum) throws SQLException {
        return new Like(
                rs.getLong("film_id"),
                rs.getLong("user_id"));
    }

    public List<Genre> getFilmGenres(Long filmId) {
        String query = "SELECT * FROM genres g, genre_film gf where gf.genre_id = g.genre_id AND gf.film_id = ?";
        List<Genre> genreList = jdbcTemplate.query(query, BuilderFilm::makeGenre, filmId);
        return genreList;
    }

    public List<FilmJoinGenre> getFilmJoinGenres(String filmsId) {
        String query = "SELECT * FROM genre_film gf " +
                "LEFT JOIN genres g ON gf.genre_id = g.genre_id " +
                "WHERE gf.film_id IN (" + filmsId + ")";
        List<FilmJoinGenre> genreList = jdbcTemplate.query(query, BuilderFilm::makeFilmJoinGenre);
        return genreList;
    }

    public Set<Long> getFilmLike(Long filmId) {
        String query = "SELECT * FROM likes WHERE film_id = ?";
        Set<Long> set = new HashSet<>();
        for (Like l : jdbcTemplate.query(query, BuilderFilm::makeLike, filmId)) {
            set.add(l.getUserId());
        }
        return set;
    }

    public List<Like> getFilmLikes(String filmsId) {
        String query = "SELECT * FROM likes WHERE film_id IN (" + filmsId + ")";
        List<Like> likesList = jdbcTemplate.query(query, BuilderFilm::makeLike);
        return likesList;
    }

    public Film build(Film film) {
        long id = film.getId();
        film.setGenres(getFilmGenres(id));
        film.setLikes(getFilmLike(id));
        return film;
    }
}