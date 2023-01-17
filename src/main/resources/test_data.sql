
INSERT INTO GENRES (NAME)
VALUES

('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');


INSERT  INTO  MPA  (NAME)
VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO USERS ( EMAIL, LOGIN, NAME, BIRTHDAY) VALUES
('email1@mail.ru', 'login1', 'name1', '2020-10-10'),
('email2@mail.ru', 'login2', 'name2', '2020-10-10'),
('email3@mail.ru', 'login3', 'name3', '2020-10-10');

INSERT INTO FILMS (NAME, DESCRIPTION, DURATION, MPA_ID, RELEASE_DATE) VALUES
('film1','desc1',150,'1', '2020-10-10'),
('film2','desc2',200,'2', '2020-10-10'),
('film3','desc3',300,'3', '2020-10-10');
