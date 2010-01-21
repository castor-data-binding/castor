CREATE TABLE test2868_otm_author (
    id INTEGER PRIMARY KEY
);

CREATE TABLE test2868_otm_book (
    id INTEGER PRIMARY KEY,
    author_id INTEGER NOT NULL,
    FOREIGN KEY (author_id) REFERENCES test2868_otm_author (id)
);