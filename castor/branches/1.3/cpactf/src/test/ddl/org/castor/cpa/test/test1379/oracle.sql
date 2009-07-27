DROP TABLE test1379_prod CASCADE CONSTRAINTS;

CREATE TABLE test1379_prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null
);

DROP TABLE test1379_computer CASCADE CONSTRAINTS;

CREATE TABLE test1379_computer (
  id   int          not null,
  cpu  varchar(200) not null
);
