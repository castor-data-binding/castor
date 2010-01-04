DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS flat;
DROP TABLE IF EXISTS house;

CREATE TABLE book(
    isbn INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    PRIMARY KEY (isbn)
);

CREATE TABLE address(
    id INTEGER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employee(
    id INTEGER NOT NULL,
    address_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id)
        REFERENCES address(id)
);

CREATE TABLE house(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE flat(
    id INTEGER NOT NULL,
    house_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (house_id)
        REFERENCES house(id)
);