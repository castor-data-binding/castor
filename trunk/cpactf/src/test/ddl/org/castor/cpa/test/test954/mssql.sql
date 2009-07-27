DROP TABLE test954_prod
GO

CREATE TABLE test954_prod(
	id int NOT NULL,
	name varchar(200) NOT NULL
) 
GO
INSERT test954_prod (id, name) VALUES (1, 'This is the test object.');
