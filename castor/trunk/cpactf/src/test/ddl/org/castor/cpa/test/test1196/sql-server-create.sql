CREATE TABLE test1196_driver(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
	car varchar(8),
PRIMARY KEY ( oid )
)
GO

CREATE TABLE test1196_car(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
	driver varchar(8),
PRIMARY KEY ( oid ) 
) 
GO

CREATE TABLE test1196_order(
	oid varchar(8) NOT NULL,
	onum int NOT NULL,
PRIMARY KEY ( oid )
)
GO

INSERT test1196_order (oid, onum) VALUES ('AAAAOR01', 1)
GO

CREATE TABLE test1196_country(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
PRIMARY KEY ( oid )
) 
GO

CREATE TABLE test1196_product(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
PRIMARY KEY ( oid ) 
)
GO

INSERT test1196_product (oid, name) VALUES ('AAAACP01', 'COMPUTER 01')
GO
INSERT test1196_product (oid, name) VALUES ('AAAACP02', 'COMPUTER 02')
GO

CREATE TABLE test1196_state(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
	country varchar(8) NOT NULL,
PRIMARY KEY ( oid ) 
) 
GO

CREATE TABLE test1196_orderitem(
	oid varchar(8) NOT NULL,
	quantity int NOT NULL,
	product varchar(8) NULL,
	parent varchar(8) NOT NULL,
PRIMARY KEY ( oid ) 
) 
GO

INSERT test1196_orderitem (oid, quantity, product, parent) VALUES ('AAOR01I1', 1, 'AAAACP01', 'AAAAOR01')
GO

CREATE TABLE test1196_computer(
	oid varchar(8) NOT NULL,
	snum varchar(20) NOT NULL,
	orderitem varchar(8),
PRIMARY KEY ( oid )
) 
GO

INSERT test1196_computer (oid, snum, orderitem) VALUES ('AAAACP01', 'CP01', 'AAOR01I1')
GO
INSERT test1196_computer (oid, snum, orderitem) VALUES ('AAAACP02', 'CP02', NULL)
GO

ALTER TABLE test1196_car ADD  CONSTRAINT test1196_driver_fk FOREIGN KEY(driver)
REFERENCES test1196_driver (oid)
GO
ALTER TABLE test1196_car CHECK CONSTRAINT test1196_driver_fk
GO
ALTER TABLE test1196_computer ADD FOREIGN KEY(orderitem)
REFERENCES test1196_orderitem (oid)
GO
ALTER TABLE test1196_computer ADD FOREIGN KEY(oid)
REFERENCES test1196_product (oid)
GO
ALTER TABLE test1196_driver ADD  CONSTRAINT test1196_car_fk FOREIGN KEY(car)
REFERENCES test1196_car (oid)
GO
ALTER TABLE test1196_driver CHECK CONSTRAINT test1196_car_fk
GO
ALTER TABLE test1196_orderitem ADD FOREIGN KEY(parent)
REFERENCES test1196_order (oid)
GO
ALTER TABLE test1196_orderitem ADD FOREIGN KEY(product)
REFERENCES test1196_product (oid)
GO
ALTER TABLE test1196_state ADD FOREIGN KEY(country)
REFERENCES test1196_country (oid)
GO
