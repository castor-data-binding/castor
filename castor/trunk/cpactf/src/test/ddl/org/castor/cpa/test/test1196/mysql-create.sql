CREATE TABLE test1196_country (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    PRIMARY KEY (oid)
);

CREATE TABLE test1196_state (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    country VARCHAR(8) NOT NULL,
    PRIMARY KEY (oid)
);

ALTER TABLE test1196_state
ADD FOREIGN KEY (country)
REFERENCES test1196_country (oid);


CREATE TABLE test1196_car (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    driver VARCHAR(8),
    PRIMARY KEY (oid)
);

CREATE TABLE test1196_driver (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    car VARCHAR(8),
    PRIMARY KEY (oid)
);

ALTER TABLE test1196_car
ADD CONSTRAINT test1196_driver_fk FOREIGN KEY (driver)
REFERENCES test1196_driver (oid);

ALTER TABLE test1196_driver
ADD CONSTRAINT test1196_car_fk FOREIGN KEY (car)
REFERENCES test1196_car (oid);


CREATE TABLE test1196_order (
    oid VARCHAR(8) NOT NULL,
    onum INTEGER NOT NULL,
    PRIMARY KEY (oid)
);

CREATE TABLE test1196_product (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    PRIMARY KEY (oid)
);

CREATE TABLE test1196_orderitem (
    oid VARCHAR(8) NOT NULL,
    quantity INTEGER NOT NULL,
    product VARCHAR(8),
    parent VARCHAR(8) NOT NULL,
    PRIMARY KEY (oid)
);

ALTER TABLE test1196_orderitem
ADD FOREIGN KEY (product)
REFERENCES test1196_product (oid);

ALTER TABLE test1196_orderitem
ADD FOREIGN KEY (parent)
REFERENCES test1196_order (oid);

CREATE TABLE test1196_computer (
    oid VARCHAR(8) NOT NULL,
    snum VARCHAR(20) NOT NULL,
    orderitem VARCHAR(8),
    PRIMARY KEY (oid)
);

ALTER TABLE test1196_computer
ADD FOREIGN KEY (oid)
REFERENCES test1196_product (oid);

ALTER TABLE test1196_computer
ADD FOREIGN KEY (orderitem)
REFERENCES test1196_orderitem (oid);
