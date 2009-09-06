CREATE TABLE test2806_parent (
    id INTEGER PRIMARY KEY,
    title VARCHAR(100)
);

CREATE TABLE test2806_child (
    id INTEGER PRIMARY KEY,
    title VARCHAR(100),
    parent INTEGER NOT NULL,
    FOREIGN KEY (parent) REFERENCES test2806_parent (id)
);
