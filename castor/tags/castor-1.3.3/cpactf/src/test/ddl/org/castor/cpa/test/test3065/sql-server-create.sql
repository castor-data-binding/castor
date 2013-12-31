CREATE TABLE test3065_extended (
  id            int NOT NULL default 0,
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
)
go

INSERT INTO test3065_extended VALUES (1, 'This is the extended object.')
go

CREATE TABLE test3065_extendedextended (
  id            int NOT NULL default 0,
  description3  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
)
go

INSERT INTO test3065_extendedextended VALUES (1, 'This is the extendedextended object.')
go

CREATE TABLE test3065_extended2 (
  id            int NOT NULL default 0,
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
)
go

INSERT INTO test3065_extended2 VALUES (2, 'This is the extended object2.')
go

CREATE TABLE test3065_object (
  id           int NOT NULL default 0,
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
)
go

INSERT INTO test3065_object VALUES (1, 'This is the test object1.', '0')
go
INSERT INTO test3065_object VALUES (2, 'This is the test object2.', '0')
go