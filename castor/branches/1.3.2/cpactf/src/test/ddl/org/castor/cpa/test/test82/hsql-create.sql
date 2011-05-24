CREATE TABLE test82_test_depends_ns_nokg (
  id int IDENTITY,
  master_id int DEFAULT 0,
  descrip varchar(50) default '',
  PRIMARY KEY  (id)
);

CREATE TABLE test82_test_master_ns_nokg (
  id int IDENTITY,
  descrip varchar(50) default '',
  PRIMARY KEY  (id)
);