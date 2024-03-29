CREATE TABLE IF NOT EXISTS users (
user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
email varchar(40) NOT NULL,
name varchar(40),
login varchar NOT NULL,
birthday date,
CONSTRAINT user_constr CHECK (email <> '' AND login <> '' AND birthday <= NOW())
);

CREATE TABLE IF NOT EXISTS mpa (
mpa_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
mpa_name varchar(40)
);

CREATE TABLE IF NOT EXISTS film (
film_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
name varchar(40) NOT NULL,
description varchar(200),
release_date date,
duration int,
mpa_id int REFERENCES mpa (mpa_id),
CONSTRAINT film_constr CHECK (name <> '' AND release_date > '1895-12-28' AND duration > 0)
);

CREATE TABLE IF NOT EXISTS friends (
user_id integer NOT NULL,
friend_id integer NOT NULL,
CONSTRAINT friendship_pk PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
film_id integer NOT NULL,
user_id integer NOT NULL,
CONSTRAINT like_pk PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS genre (
genre_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
name varchar(40)
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id integer NOT NULL,
genre_id integer NOT NULL,
CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id)
);