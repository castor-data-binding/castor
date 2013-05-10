CREATE TABLE test81_test_depends_ns (
  id int NOT NULL,
  master_id int NOT NULL,
  descrip varchar(50) NOT NULL,
  primary key (id)
)
/

CREATE TABLE test81_test_master_ns (
  id int NOT NULL,
  descrip varchar(50) NOT NULL,
  primary key (id)
)
/
