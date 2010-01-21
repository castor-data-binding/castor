CREATE TABLE test2860_mto_author (
    id INTEGER PRIMARY KEY,
    time_stamp bigint,
    name varchar(100)
);

CREATE TABLE test2860_mto_book (
    id INTEGER PRIMARY KEY,
    author_id INTEGER NOT NULL,
    time_stamp bigint,
    name varchar(100),
    FOREIGN KEY (author_id) REFERENCES test2860_mto_author (id)
);