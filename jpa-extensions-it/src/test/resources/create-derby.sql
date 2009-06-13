CREATE SCHEMA TEST;

CREATE TABLE single_staff(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    pay NUMERIC(7,2) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE OneToOne_show(
    id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    timeslot_id INTEGER NOT NULL,
    rating_id INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE OneToOne_timeslot(
    id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE OneToOne_rating(
    id INTEGER NOT NULL,
    value INTEGER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE OneToMany_actor(
    svnr INTEGER NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    surname VARCHAR(200) NOT NULL,
    PRIMARY KEY (svnr)
);

CREATE TABLE OneToMany_role(
    id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    actor_id INTEGER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ManyToMany_author(
    id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ManyToMany_book(
    isbn INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    PRIMARY KEY (isbn)
);

CREATE TABLE ManyToMany_books_authors(
    book_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL
);
