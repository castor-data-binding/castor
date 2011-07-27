CREATE TABLE test82_test_depends_ns_nokg (
  id int NOT NULL,
  master_id int NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  primary key (id)
)
go

CREATE TABLE test82_test_master_ns_nokg (
  id int NOT NULL,
  descrip varchar(50) NOT NULL default '',
  primary key (id)
)
go