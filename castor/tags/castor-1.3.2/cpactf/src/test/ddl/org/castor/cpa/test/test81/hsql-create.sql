CREATE TABLE test81_test_depends_ns (
  id int  IDENTITY,
  master_id int,
  descrip varchar(50) default '',
  primary key (id)
);

CREATE TABLE test81_test_master_ns (
  id int IDENTITY,
  descrip varchar(50) default '',
  primary key (id)
);