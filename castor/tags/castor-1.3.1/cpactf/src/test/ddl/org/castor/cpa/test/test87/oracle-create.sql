CREATE TABLE test87_entity (
  id        int           not null,
  name      varchar(200)  not null,
  time_stamp numeric(18,0) not null
)
/
CREATE UNIQUE INDEX test87_entity_pk ON test87_entity ( id )
/

CREATE TABLE test87_product (
  id        int           not null,
  name      varchar(200)  not null,
  group_id  int           not null,
  time_stamp numeric(18,0) not null
)
/
CREATE UNIQUE INDEX test87_product_pk ON test87_product ( id )
/

CREATE TABLE test87_group (
  id        int           not null,
  name      varchar(200)  not null,
  time_stamp numeric(18,0) not null
)
/
CREATE UNIQUE INDEX test87_group_pk ON test87_group ( id )
/

CREATE TABLE test87_base (
  id        int           not null,
  name      varchar(200)  not null,
  time_stamp numeric(18,0) not null
)
/
CREATE UNIQUE INDEX test87_base_pk ON test87_base ( id )
/

CREATE TABLE test87_extended (
  id        int          not null,
  note      varchar(200) not null
)
/
CREATE UNIQUE INDEX test87_extended_pk ON test87_extended ( id )
/
