CREATE TABLE test81_test_depends_ns (
  id serial NOT NULL,
  master_id int NOT NULL DEFAULT 0,
  descrip varchar(50) NOT NULL default '',
  primary key (id)
);

CREATE TABLE test81_test_master_ns (
  id serial NOT NULL,
  descrip varchar(50) NOT NULL default '',
  primary key (id)
);
