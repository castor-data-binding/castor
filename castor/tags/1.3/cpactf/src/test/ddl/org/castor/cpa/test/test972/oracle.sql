DROP TABLE test972_newprod CASCADE CONSTRAINTS;

CREATE TABLE test972_newprod (
  ID       numeric(22)    not null,
  NAME     varchar(200)   not null,
  PRICE    numeric(18,2)  not null,
  GROUP_ID numeric(22)    not null
);

DROP TABLE test972_prod CASCADE CONSTRAINTS;

CREATE TABLE test972_prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null,
  group_id  int           not null
);

DROP TABLE test972_prod_group CASCADE CONSTRAINTS;

CREATE TABLE test972_prod_group (
  id    int          not null,
  name  varchar(200) not null
);
