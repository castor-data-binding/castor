CREATE SCHEMA TEST;

CREATE TABLE identity_generator (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
    PRIMARY KEY(id),
    name VARCHAR(32)
);

CREATE TABLE generator_table (
    id_name VARCHAR(32),
    id_value INTEGER
);
INSERT INTO generator_table VALUES('ID_GEN', NULL);

CREATE TABLE table_key_generator (
    custom_id_name VARCHAR(32),
    custom_id_value INTEGER
);
INSERT INTO table_key_generator VALUES('FIELD_GEN', NULL);
INSERT INTO table_key_generator VALUES('CLASS_GEN', NULL);

CREATE TABLE auto_annotated_class (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE annotated_field_with_default_table_generator_definition (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE annotated_class_with_default_table_generator_definition (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE table_generator_field_subject (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE table_generator_class_subject (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE SEQUENCE sequence_generator_class_with_default_sequence_name_seq START WITH 1;
CREATE TABLE sequence_generator_class_with_default_sequence_name (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE SEQUENCE sequence_generator_field_with_default_sequence_name_seq START WITH 1;
CREATE TABLE sequence_generator_field_with_default_sequence_name (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE SEQUENCE class_sequence START WITH 1;
CREATE TABLE sequence_generator_class (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE SEQUENCE field_sequence START WITH 1;
CREATE TABLE sequence_generator_field (
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);


CREATE SEQUENCE non_cached_version_sequence START WITH 1;
CREATE TABLE non_cached_version (
    id INTEGER NOT NULL,
    PRIMARY KEY(id),
    version BIGINT,
    name VARCHAR(32)
);

CREATE SEQUENCE version_sequence START WITH 1;
CREATE TABLE version (
    id INTEGER NOT NULL,
    PRIMARY KEY(id),
    version BIGINT,
    name VARCHAR(32)
);

CREATE TABLE Inheritance_Plant(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Inheritance_Tree(
    id INTEGER NOT NULL,
    height INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE MappedSuperclass_keyboard(
    id INTEGER NOT NULL,
    numberOfKeys INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    price INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE Cache_none(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Cache_unlimited(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Cache_limited(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE NamedNativeQueries_student(
    id INTEGER NOT NULL,
    firstname VARCHAR(25) NOT NULL,
    lastname VARCHAR(25) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE NamedQueries_employee(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE NamedQueries_person(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Callbacks_person(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Callbacks_cat(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Callbacks_martian(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE EntityListeners_animal(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE EntityListeners_pet(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE EntityListeners_dog(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE EntityListeners_retriever(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE ExcludeListeners_foo(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE ExcludeListeners_bar(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Temporal_person(
    id INTEGER NOT NULL,
    birthDate TIMESTAMP NOT NULL,
    anotherDate TIME NOT NULL,
    yetAnotherDate DATE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Lob_entity(
    id INTEGER NOT NULL,
    clob CLOB NOT NULL,
    blob BLOB NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Enum_entity(
    id INTEGER NOT NULL,
    stringEnum VARCHAR(50) NOT NULL,
    ordinalEnum INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE single_staff(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    pay NUMERIC(7,2) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE staff_version(
    id INTEGER NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    version BIGINT,
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
