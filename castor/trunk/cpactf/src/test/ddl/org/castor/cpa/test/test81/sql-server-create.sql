CREATE TABLE test81_test_depends_ns (
  id int identity NOT NULL,
  master_id int NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  primary key (id)
)
go

CREATE TABLE test81_test_master_ns (
  id int identity NOT NULL,
  descrip varchar(50) NOT NULL default '',
  primary key (id)
)
go
