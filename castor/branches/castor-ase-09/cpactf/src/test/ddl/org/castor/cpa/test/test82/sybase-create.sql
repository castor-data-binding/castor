CREATE TABLE test82_test_depends_ns_nokg (
  id int(11) NOT NULL,
  master_id int(11) NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  primary key (id),
  KEY master_id (master_id)
)
go

CREATE TABLE test82_test_master_ns_nokg (
  id int(11) NOT NULL,
  descrip varchar(50) NOT NULL default '',
  primary key (id)
)
go