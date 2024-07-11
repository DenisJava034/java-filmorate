INSERT INTO genres (NAME)
	SELECT ('Комедия') WHERE NOT EXISTS (SELECT * FROM genres WHERE NAME = 'Комедия');
INSERT INTO genres (NAME)
	SELECT ('Драма') WHERE NOT EXISTS (SELECT * FROM genres WHERE NAME = 'Драма');
INSERT INTO genres (NAME)
	SELECT ('Мультфильм') WHERE NOT EXISTS (SELECT * FROM genres WHERE NAME = 'Мультфильм');
INSERT INTO genres (NAME)
	SELECT ('Триллер') WHERE NOT EXISTS (SELECT * FROM genres WHERE NAME = 'Триллер');
INSERT INTO genres (NAME)
	SELECT ('Документальный') WHERE NOT EXISTS (SELECT * FROM genres WHERE NAME = 'Документальный');
INSERT INTO genres (NAME)
	SELECT ('Боевик') WHERE NOT EXISTS (SELECT * FROM genres WHERE NAME = 'Боевик');

INSERT INTO FILM_RATING (NAME, description)
SELECT 'G', 'у фильма нет возрастных ограничений'




WHERE NOT EXISTS (SELECT name, description FROM FILM_RATING WHERE NAME = 'G');
INSERT INTO FILM_RATING (NAME, description)
SELECT 'PG', 'детям рекомендуется смотреть фильм с родителями'
WHERE NOT EXISTS (SELECT name, description FROM FILM_RATING WHERE NAME = 'PG');
INSERT INTO FILM_RATING (NAME, description)
SELECT 'PG-13', 'детям до 13 лет просмотр не желателен'
WHERE NOT EXISTS (SELECT name, description FROM FILM_RATING WHERE NAME = 'PG-13');
INSERT INTO FILM_RATING (NAME, description)
SELECT 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
WHERE NOT EXISTS (SELECT name, description FROM FILM_RATING WHERE NAME = 'R');
INSERT INTO FILM_RATING (NAME, description)
SELECT 'NC-17', 'лицам до 18 лет просмотр запрещён'
WHERE NOT EXISTS (SELECT name, description FROM FILM_RATING WHERE NAME = 'NC-17');

INSERT INTO USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES('user1@jhbb.ru', 'user1', 'User1', '2024-12-01');
INSERT INTO USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES('user2@jhbb.ru', 'user1', 'User1', '2024-12-01');
INSERT INTO USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES('user3@jhbb.ru', 'user1', 'User1', '2024-12-01');

INSERT INTO FILMS
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_RATING)
VALUES('film1', 'film1', '2024-04-01', 1, 1);

INSERT INTO FILMS
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_RATING)
VALUES('film2', 'film2', '2024-04-01', 1, 1);

INSERT INTO FILMS
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_RATING)
VALUES('film2', 'film2', '2024-04-01', 1, 1);