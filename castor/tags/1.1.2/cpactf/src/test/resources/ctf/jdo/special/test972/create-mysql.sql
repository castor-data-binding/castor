drop table if exists newprod;

create table newprod (
  ID       numeric(22)    not null,
  NAME     varchar(200)   not null,
  PRICE    numeric(18,2)  not null,
  GROUP_ID numeric(22)    not null
);

drop table if exists prod;

create table prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null,
  group_id  int           not null
);

drop table if exists prod_group;

create table prod_group (
  id    int          not null,
  name  varchar(200) not null
);
