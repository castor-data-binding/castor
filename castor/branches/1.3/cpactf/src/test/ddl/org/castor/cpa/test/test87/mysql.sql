drop table if exists test87_entity;

create table test87_entity (
  id        int          not null,
  name      varchar(200) not null,
  timestamp bigint       not null
);

create unique index test87_entity_pk on test87_entity ( id );



drop table if exists test87_product;

create table test87_product (
  id        int          not null,
  name      varchar(200) not null,
  group_id  int          not null,
  timestamp bigint       not null
);

create unique index test87_product_pk on test87_product ( id );

drop table if exists test87_group;

create table test87_group (
  id        int          not null,
  name      varchar(200) not null,
  timestamp bigint       not null
);

create unique index test87_group_pk on test87_group ( id );



drop table if exists test87_extended;
drop table if exists test87_base;

create table test87_base (
  id        int          not null,
  name      varchar(200) not null,
  timestamp bigint       not null
);

create unique index test87_base_pk on test87_base ( id );

create table test87_extended (
  id        int          not null,
  note      varchar(200) not null
);

create unique index test87_extended_pk on test87_extended ( id );
