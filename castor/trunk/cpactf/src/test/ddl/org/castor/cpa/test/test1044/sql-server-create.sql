CREATE TABLE test1044_base_object (
  id           int NOT NULL default 0,
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
)
go

INSERT INTO test1044_base_object VALUES (1, 'This is the test object.', '0')
go
