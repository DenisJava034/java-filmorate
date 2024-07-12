insert into genres (NAME)
	select ('Комедия') where not exists (select * from genres where NAME = 'Комедия');
insert into genres (NAME)
	select ('Драма') where not exists (select * from genres where NAME = 'Драма');
insert into genres (NAME)
	select ('Мультфильм') where not exists (select * from genres where NAME = 'Мультфильм');
insert into genres (NAME)
	select ('Триллер') where not exists (select * from genres where NAME = 'Триллер');
insert into genres (NAME)
	select ('Документальный') where not exists (select * from genres where NAME = 'Документальный');
insert into genres (NAME)
	select ('Боевик') where not exists (select * from genres where NAME = 'Боевик');

insert into FILM_RATING (NAME, description)
select 'G', 'у фильма нет возрастных ограничений'
where not exists (select name, description from FILM_RATING where NAME = 'G');
insert into FILM_RATING (NAME, description)
select 'PG', 'детям рекомендуется смотреть фильм с родителями'
where not exists (select name, description from FILM_RATING where NAME = 'PG');
insert into FILM_RATING (NAME, description)
select 'PG-13', 'детям до 13 лет просмотр не желателен'
where not exists (select name, description from FILM_RATING where NAME = 'PG-13');
insert into FILM_RATING (NAME, description)
select 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
where not exists (select name, description from FILM_RATING where NAME = 'R');
insert into FILM_RATING (NAME, description)
select 'NC-17', 'лицам до 18 лет просмотр запрещён'
where not exists (select name, description from FILM_RATING where NAME = 'NC-17');

insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user1@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user2@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user3@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user1@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user2@jhbb.ru', 'user1', 'User1', '2024-12-01');
insert into USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
values('user3@jhbb.ru', 'user1', 'User1', '2024-12-01');
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