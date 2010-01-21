CREATE TABLE test2860_mtm_author (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    time_stamp BIGINT
);

CREATE TABLE test2860_mtm_book (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    time_stamp BIGINT
);

CREATE TABLE test2860_book_author (
    author_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    FOREIGN KEY (author_id) REFERENCES test2860_mtm_author (id),
    FOREIGN KEY (book_id) REFERENCES test2860_mtm_book (id)
);