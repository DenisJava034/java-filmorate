package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface IntefaceService<T> {
    Collection<T> getAll();

    T getById(Long id);

    T create(T data);

    T update(T data);

    void delete();

    void deleteById(Long id);
}
