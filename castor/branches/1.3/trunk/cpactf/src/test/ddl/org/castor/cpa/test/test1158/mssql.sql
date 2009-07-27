DROP TABLE test1158_extended
GO
DROP TABLE test1158_object
GO

CREATE TABLE test1158_extended(
	id int NOT NULL DEFAULT '0',
	description2 varchar(50) NOT NULL DEFAULT '',
PRIMARY KEY ( id ) 
)
GO

CREATE TABLE test1158_object(
	id int NOT NULL DEFAULT '0',
	description varchar(50) NOT NULL DEFAULT '',
	saved char(1) DEFAULT '0',
PRIMARY KEY ( id )
)
GO

INSERT test1158_extended (id, description2) VALUES (1, 'This is the extended object.');

INSERT test1158_object (id, description, saved) VALUES (1, 'This is the test object.', '');
