MERGE INTO genres (GENRE_ID, NAME)
	VALUES (1,'Комедия');

MERGE INTO genres (GENRE_ID, NAME)
	VALUES (2, 'Драма');

MERGE INTO genres (GENRE_ID, NAME)
	VALUES (3, 'Мультфильм');

MERGE INTO genres (GENRE_ID, NAME)
	VALUES (4, 'Триллер');

MERGE INTO genres (GENRE_ID, NAME)
	VALUES (5, 'Документальный');

MERGE INTO genres (GENRE_ID, NAME)
	VALUES (6, 'Боевик');

MERGE INTO FILM_RATING (FILM_RATING_ID, NAME, description)
VALUES (1, 'G', 'у фильма нет возрастных ограничений');

MERGE INTO FILM_RATING (FILM_RATING_ID, NAME, description)
VALUES (2, 'PG', 'детям рекомендуется смотреть фильм с родителями');

MERGE INTO FILM_RATING (FILM_RATING_ID, NAME, description)
VALUES (3, 'PG-13', 'детям до 13 лет просмотр не желателен');

MERGE INTO FILM_RATING (FILM_RATING_ID, NAME, description)
VALUES (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого');

MERGE INTO FILM_RATING (FILM_RATING_ID, NAME, description)
VALUES  (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');

