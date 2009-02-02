DROP TABLE test881_prod
GO

CREATE TABLE test881_prod(
	id int NOT NULL,
	name varchar(200) NOT NULL
)
GO
INSERT test881_prod (id, name) VALUES (1, 'This is the test object.');
