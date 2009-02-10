DROP TABLE test87_entity CASCADE CONSTRAINTS;

CREATE TABLE test87_entity (
  id        int           not null,
  name      varchar(200)  not null,
  timestamp numeric(18,0) not null
);

CREATE UNIQUE INDEX test87_entity_pk ON test87_entity ( id );



DROP TABLE test87_product CASCADE CONSTRAINTS;

CREATE TABLE test87_product (
  id        int           not null,
  name      varchar(200)  not null,
  group_id  int           not null,
  timestamp numeric(18,0) not null
);

CREATE UNIQUE INDEX test87_product_pk ON test87_product ( id );

DROP TABLE test87_group CASCADE CONSTRAINTS;

CREATE TABLE test87_group (
  id        int           not null,
  name      varchar(200)  not null,
  timestamp numeric(18,0) not null
);

CREATE UNIQUE INDEX test87_group_pk ON test87_group ( id );



DROP TABLE test87_extended CASCADE CONSTRAINTS;
DROP TABLE test87_base CASCADE CONSTRAINTS;

CREATE TABLE test87_base (
  id        int           not null,
  name      varchar(200)  not null,
  timestamp numeric(18,0) not null
);

CREATE UNIQUE INDEX test87_base_pk ON test87_base ( id );

CREATE TABLE test87_extended (
  id        int          not null,
  note      varchar(200) not null
);

CREATE UNIQUE INDEX test87_extended_pk ON test87_extended ( id );
