DROP TABLE test87_entity
GO

DROP TABLE test87_product
GO

DROP TABLE test87_group
GO

DROP TABLE test87_extended
GO

DROP TABLE test87_base
GO

CREATE TABLE test87_entity (
  id        int          not null,
  name      varchar(200) not null,
  timestamp bigint       not null
)
GO

CREATE UNIQUE INDEX test87_entity_pk on test87_entity ( id )
GO

CREATE TABLE test87_product (
  id        int          not null,
  name      varchar(200) not null,
  group_id  int          not null,
  timestamp bigint       not null
)
GO

CREATE UNIQUE INDEX test87_product_pk on test87_product ( id )
GO

CREATE TABLE test87_group (
  id        int          not null,
  name      varchar(200) not null,
  timestamp bigint       not null
)
GO

CREATE UNIQUE INDEX test87_group_pk on test87_group ( id )
GO

CREATE TABLE test87_base (
  id        int          not null,
  name      varchar(200) not null,
  timestamp bigint       not null
)
GO

CREATE UNIQUE INDEX test87_base_pk on test87_base ( id )
GO

CREATE TABLE test87_extended (
  id        int          not null,
  note      varchar(200) not null
)
GO

CREATE UNIQUE INDEX test87_extended_pk on test87_extended ( id )
GO
