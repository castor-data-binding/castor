DROP TABLE test1002_prod
GO
CREATE TABLE test1002_prod(
	id int NOT NULL,
	name varchar(200) NOT NULL
) 
GO
INSERT test1002_prod (id, name) VALUES (1, 'This is the test object.');
