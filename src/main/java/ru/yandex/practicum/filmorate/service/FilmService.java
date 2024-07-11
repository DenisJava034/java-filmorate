package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.builders.BuilderFilm;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmJoinGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.ListGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService implements IntefaceService<Film> {
    private static final LocalDate DATE_MARK = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final BuilderFilm builderFilm;
    private final GenreService genreService;
    private final ListGenreDbStorage listGenreDbStorage;
    private final MpaService mpaService;

    public Collection<Film> getAll() {
        List<Film> filmList = filmStorage.findAllFilms();
        Map<Long, Film> filmMap = new HashMap<>();
        filmList.stream()
                .map(film -> filmMap.put(film.getId(), film))
                .toList();
        List<Long> longList = filmMap.keySet().stream().toList();
        String stringJoinLongFilmId = longList.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","));
        List<FilmJoinGenre> filmJoinGenres = builderFilm.getFilmJoinGenres(stringJoinLongFilmId);
        List<Like> likeList = builderFilm.getFilmLikes(stringJoinLongFilmId);
        for (FilmJoinGenre fjg : filmJoinGenres) {
            List<Genre> genreList = filmMap.get(fjg.getFilmId()).getGenres();

            if (genreList == null) {
                genreList = new ArrayList<>();
            }
            genreList.add(new Genre(fjg.getId(), fjg.getName()));
            filmMap.get(fjg.getFilmId()).setGenres(genreList);
        }
        for (Like l : likeList) {
            Set<Long> likeSetFilm = filmMap.get(l.getId()).getLikes();
            if (likeSetFilm == null) {
                likeSetFilm = new HashSet<>();
            }
            likeSetFilm.add(l.getUserId());
            filmMap.get(l.getId()).setLikes(likeSetFilm);
        }
        return filmMap.values();
    }

    public Film getById(Long id) {
        Film film = filmStorage.getById(id).orElseThrow(() -> new NotFoundException("Нет film с заданным ID"));
        return builderFilm.build(film);

    }

    public List<Film> getPopular(Long count) {
        return filmStorage.getPopular(count)
                .stream().map(builderFilm::build)
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .toList();
    }

    public Film create(Film data) {
        validate(data);
        if (data.getMpa() != null) {
            try {
                mpaService.getById(data.getMpa().getId());
            } catch (NotFoundException e) {
                throw new ValidationException("нет mpa с заданным id:" + data.getMpa().getId());
            }
        }
        if (data.getGenres() != null) {
            for (Genre g : data.getGenres()) {
                try {
                    genreService.getById(g.getId());
                } catch (NotFoundException e) {
                    throw new ValidationException("нет genre с заданным id:" + g.getId());
                }
            }
        }
        Film film = filmStorage.createFilms(data).get();
        if (data.getGenres() != null) {
            if (!data.getGenres().isEmpty()) {
                listGenreDbStorage.addGenreToListGenges(data, film.getId());
            }
        }

        log.debug("Film создан" + data);
        return getById(film.getId());
    }

    public Film update(Film data) {
        if (data.getId() == null) {
            throw new ValidationException("ID не должен содержать NULL");
        }
        filmStorage.getById(data.getId());
        validate(data);
        log.debug("Film обновлен" + data);
        return builderFilm.build(filmStorage.updateFilms(data).get());
    }

    @Override
    public void delete() {
        filmStorage.delete();
    }

    @Override
    public void deleteById(Long id) {
        filmStorage.deleteById(id);
    }

    public Film addLike(long filmId, long userId) {
        Film film = getById(filmId);
        userService.getById(userId);
        filmStorage.addLike(filmId, userId);
        return getById(filmId);
    }

    public Optional<Film> deleteLike(long filmId, long userId) {
        getById(filmId);
        userService.getById(userId);
        filmId = filmStorage.deleteLike(filmId, userId).get().getId();
        return Optional.of(getById(filmId));
    }

    private void validate(final Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(DATE_MARK)) {
            final String s = "Дата релиза — не раньше 28 декабря 1895 года";
            log.info("Вызвано исключение: " + s + " Получено: " + film.getReleaseDate());
            throw new ValidationException(s);
        }
    }
}