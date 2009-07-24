CREATE TABLE detachment_book (
    id INTEGER PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    timestamp INTEGER NOT NULL
);

CREATE TABLE detachment_employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    version BIGINT NOT NULL
);