DROP TABLE test881_prod CASCADE CONSTRAINTS;

CREATE TABLE test881_prod (
  id        int           not null,
  name      varchar(200)  not null
);

INSERT INTO test881_prod VALUES (1, 'This is the test object.');
