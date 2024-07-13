MERGE INTO genres (GENRE_ID, NAME)
	VALUES (1,'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO FILM_RATING (FILM_RATING_ID, NAME, description)
VALUES (1, 'G', 'у фильма нет возрастных ограничений'),
       (2, 'PG', 'детям рекомендуется смотреть фильм с родителями'),
       (3, 'PG-13', 'детям до 13 лет просмотр не желателен'),
       (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');


insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user1@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user2@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user3@jhbb.ru', 'user1', 'User1', '2024-12-01');

insert into FILMS
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_RATING)
values('film1', 'film1', '2024-04-01', 1, 1);

insert into FILMS
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_RATING)
values('film2', 'film2', '2024-04-01', 1, 1);

insert into FILMS
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_RATING)
values('film2', 'film2', '2024-04-01', 1, 1);