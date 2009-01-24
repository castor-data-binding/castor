DROP TABLE IF EXISTS test30_extends;
DROP TABLE IF EXISTS test30_entity;

CREATE TABLE test30_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);
CREATE UNIQUE INDEX test30_entity_pk ON test30_entity ( id );

CREATE TABLE test30_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
);
CREATE UNIQUE INDEX test30_extends_pk ON test30_extends ( id );

DROP TABLE IF EXISTS test30_group;
CREATE TABLE test30_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)  not null
);
CREATE UNIQUE INDEX test30_group_pk ON test30_group ( id );
