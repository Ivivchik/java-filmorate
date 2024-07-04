CREATE TABLE IF NOT EXISTS users(
    id          INT         PRIMARY KEY,
    name        VARCHAR,
    login       VARCHAR     UNIQUE NOT NULL,
    email       VARCHAR     UNIQUE,
    birthday    DATE        CHECK (birthday <= CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS genre (
    id      INT         PRIMARY KEY,
    name    VARCHAR
);

CREATE TABLE IF NOT EXISTS rating (
    id      INT         PRIMARY KEY,
    name    VARCHAR(5)
);

CREATE TABLE IF NOT EXISTS films (
    id              INT             PRIMARY KEY,
    name            VARCHAR         NOT NULL,
    description     VARCHAR(200),   CHECK (LENGTH(description) <= 200),
    release_date    DATE            CHECK (release_date > '1985-12-28'),
    duration        INT             CHECK (duration > 0),
    rating_id       INT,
    FOREIGN KEY (rating_id) REFERENCES rating(id)
);

CREATE TABLE IF NOT EXISTS friends (
    user_id     INT,
    friend_id   INT,
    approved    BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS user_like_film (
    film_id     INT,
    user_id     INT,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id     INT,
    genre_id    INT,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (genre_id) REFERENCES genre(id)
);