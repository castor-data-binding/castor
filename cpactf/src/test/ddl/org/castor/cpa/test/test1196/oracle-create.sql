CREATE TABLE test1196_country (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL
)
/
ALTER TABLE test1196_country ADD PRIMARY KEY (oid)
/

CREATE TABLE test1196_state (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    country VARCHAR(8) NOT NULL
)
/
ALTER TABLE test1196_state ADD PRIMARY KEY (oid)
/
ALTER TABLE test1196_state ADD FOREIGN KEY (country)
REFERENCES test1196_country (oid)
/

CREATE TABLE test1196_car (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    driver VARCHAR(8)
)
/
ALTER TABLE test1196_car ADD PRIMARY KEY (oid)
/

CREATE TABLE test1196_driver (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    car VARCHAR(8)
)
/
ALTER TABLE test1196_driver ADD PRIMARY KEY (oid)
/
ALTER TABLE test1196_driver ADD FOREIGN KEY (car)
REFERENCES test1196_car (oid)
/

ALTER TABLE test1196_car ADD FOREIGN KEY (driver)
REFERENCES test1196_driver (oid)
/

CREATE TABLE test1196_order (
    oid VARCHAR(8) NOT NULL,
    onum INTEGER NOT NULL
)
/
ALTER TABLE test1196_order ADD PRIMARY KEY (oid)
/

CREATE TABLE test1196_product (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL
)
/
ALTER TABLE test1196_product ADD PRIMARY KEY (oid)
/

CREATE TABLE test1196_orderitem (
    oid VARCHAR(8) NOT NULL,
    quantity INTEGER NOT NULL,
    product VARCHAR(8),
    parent VARCHAR(8) NOT NULL
)
/
ALTER TABLE test1196_orderitem ADD PRIMARY KEY (oid)
/
ALTER TABLE test1196_orderitem ADD FOREIGN KEY (product)
REFERENCES test1196_product (oid)
/
ALTER TABLE test1196_orderitem ADD FOREIGN KEY (parent)
REFERENCES test1196_order (oid)
/

CREATE TABLE test1196_computer (
    oid VARCHAR(8) NOT NULL,
    snum VARCHAR(20) NOT NULL,
    orderitem VARCHAR(8)
)
/
ALTER TABLE test1196_computer ADD PRIMARY KEY (oid)
/
ALTER TABLE test1196_computer ADD FOREIGN KEY (oid)
REFERENCES test1196_product (oid)
/
ALTER TABLE test1196_computer ADD FOREIGN KEY (orderitem)
REFERENCES test1196_orderitem (oid)
/
