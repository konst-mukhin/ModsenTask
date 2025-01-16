CREATE SEQUENCE IF NOT EXISTS _book_status_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS _user_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE _book_status
(
    id         INTEGER NOT NULL,
    book_id    INTEGER,
    is_taken   BOOLEAN,
    is_deleted BOOLEAN,
    taken      time WITHOUT TIME ZONE,
    returned   time WITHOUT TIME ZONE,
    deleted    time WITHOUT TIME ZONE,
    CONSTRAINT pk__book_status PRIMARY KEY (id)
);

CREATE TABLE _user
(
    id       INTEGER NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    role     VARCHAR(255),
    CONSTRAINT pk__user PRIMARY KEY (id)
);