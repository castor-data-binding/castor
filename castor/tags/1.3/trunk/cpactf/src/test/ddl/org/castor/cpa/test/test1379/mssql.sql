DROP TABLE test1379_computer
GO

DROP TABLE test1379_prod
GO

CREATE TABLE test1379_prod(
	id int NOT NULL,
	name varchar(200) NOT NULL,
	price numeric(18, 2) NOT NULL
)
GO

CREATE TABLE test1379_computer(
	id int NOT NULL,
	cpu varchar(200) NOT NULL
)
GO
