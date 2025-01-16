CREATE SEQUENCE IF NOT EXISTS _book_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS _user_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE _book
(
    id          INTEGER NOT NULL,
    isbn        VARCHAR(255),
    title       VARCHAR(255),
    genre       VARCHAR(255),
    description VARCHAR(255),
    author      VARCHAR(255),
    CONSTRAINT pk__book PRIMARY KEY (id)
);

CREATE TABLE _user
(
    id       INTEGER NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    role     VARCHAR(255),
    CONSTRAINT pk__user PRIMARY KEY (id)
);