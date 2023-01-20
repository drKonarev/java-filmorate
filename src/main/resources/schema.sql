
DROP TABLE IF EXISTS GENRES CASCADE;
DROP TABLE IF EXISTS MPA CASCADE;

DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS FRIENDS CASCADE;
DROP TABLE IF EXISTS FILMS CASCADE;
DROP TABLE IF EXISTS FILM_GENRE CASCADE;
DROP TABLE IF EXISTS LIKES CASCADE;



  create TABLE if not exists   FILMS
(
    FILM_ID      serial primary key ,
    NAME         VARCHAR(50)  not null UNIQUE,
    DESCRIPTION  VARCHAR(255) not null,
    DURATION     INTEGER                not null,
    MPA_ID       INTEGER                not null,
    RELEASE_DATE DATE                   not null
);

 create table if not exists  GENRES
(
    GENRE_ID serial primary key,
    NAME     VARCHAR(15) not null UNIQUE
);


 create table if not exists  FILM_GENRE
(
    FILM_ID  INTEGER not null references films(film_id) on delete cascade,
    GENRE_ID INTEGER not null references genres(genre_id)  on delete cascade
);

 create table if not exists  MPA
(
    MPA_ID serial primary key,
    NAME   VARCHAR(10) not null unique
);


alter table films
 add  foreign key (MPA_ID) references MPA(MPA_ID);
 --добавление связи после создания таблицы с рейтингами





 create table if not exists  USERS
(
    USER_ID  serial primary key ,
    EMAIL    VARCHAR(50) not null UNIQUE,
    LOGIN    VARCHAR(50) not null,
    NAME     VARCHAR(50) not null,
    BIRTHDAY DATE   not null
);

 create table if not exists LIKES
(
    USER_ID INTEGER   not null references users(user_id) on delete cascade,
    FILM_ID INTEGER   not null references films(film_id)on delete cascade,
    CONSTRAINT IF NOT EXISTS user_likes_key_unique UNIQUE(USER_ID, FILM_ID)
   -- STATUS CHARACTER not null
);



create table if not exists FRIENDS
(
    USER_ID   INTEGER not null references users(user_id) on delete cascade,
    FRIEND_ID INTEGER not null references users(user_id) on delete cascade,
    CONSTRAINT IF NOT EXISTS user_friends_key_unique UNIQUE(USER_ID, FRIEND_ID)

);


