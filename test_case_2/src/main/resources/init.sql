CREATE TABLE Student(
    id          SERIAL   PRIMARY KEY,
    name        TEXT        NOT NULL,
    surname     TEXT        NOT NULL,
    patronymic  TEXT,
    birthdate   DATE,
    stgroup       TEXT,
    number      NUMERIC
)