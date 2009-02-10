DROP TABLE test1002_prod CASCADE CONSTRAINTS;

CREATE TABLE test1002_prod (
  id        int           not null,
  name      varchar(200)  not null
);

INSERT INTO test1002_prod VALUES (1, 'This is the test object.');
