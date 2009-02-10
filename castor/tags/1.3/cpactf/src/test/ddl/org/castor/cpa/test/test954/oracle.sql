DROP TABLE test954_prod CASCADE CONSTRAINTS;

CREATE TABLE test954_prod (
  id        int           not null,
  name      varchar(200)  not null
);

INSERT INTO test954_prod VALUES (1, 'This is the test object.');
