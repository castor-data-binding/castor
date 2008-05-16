drop table if exists test972_newprod;

create table test972_newprod (
  ID       numeric(22)    not null,
  NAME     varchar(200)   not null,
  PRICE    numeric(18,2)  not null,
  GROUP_ID numeric(22)    not null
);

drop table if exists test972_prod;

create table test972_prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null,
  group_id  int           not null
);

drop table if exists test972_prod_group;

create table test972_prod_group (
  id    int          not null,
  name  varchar(200) not null
);
