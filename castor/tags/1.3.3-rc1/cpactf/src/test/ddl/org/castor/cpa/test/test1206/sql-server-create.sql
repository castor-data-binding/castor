CREATE TABLE test1206_country(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
    PRIMARY KEY ( oid )
)
GO

CREATE TABLE test1206_state(
	oid varchar(8) NOT NULL,
	name varchar(60) NOT NULL,
	country varchar(8) NOT NULL,
    PRIMARY KEY ( oid ) 
)
GO
ALTER TABLE test1206_state 
ADD FOREIGN KEY(country)
REFERENCES test1206_country (oid)
GO
