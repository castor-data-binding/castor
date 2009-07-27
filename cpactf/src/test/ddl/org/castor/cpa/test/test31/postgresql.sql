DROP TABLE test31_relation;
DROP TABLE test31_extends2;
DROP TABLE test31_extends1;
DROP TABLE test31_related;
DROP TABLE test31_persistent;
DROP TABLE test31_group;

CREATE TABLE test31_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
);
CREATE UNIQUE INDEX test31_group_pk ON test31_group ( id );

CREATE TABLE test31_persistent (
  id        integer                       not null,
  ctime     timestamp without time zone   not null,
  mtime     timestamp without time zone   null,
  value1    varchar(200)                  not null,
  parent_id integer                       null,
  group_id  numeric(10,0)                 not null
);
CREATE UNIQUE INDEX test31_persistent_pk ON test31_persistent ( id );

CREATE TABLE test31_related (
  id          integer     not null,
  persist_id  integer     not null
);
CREATE UNIQUE INDEX test31_related_pk ON test31_related ( id );

CREATE TABLE test31_extends1 (
  ident   integer         not null,
  ext     integer         not null
);
CREATE UNIQUE INDEX test31_extends1_pk ON test31_extends1 ( ident );

CREATE TABLE test31_extends2 (
  id      integer         not null,
  ext     integer         not null
);
CREATE UNIQUE INDEX test31_extends2_pk ON test31_extends2 ( id );

CREATE TABLE test31_relation (
  id1   integer         not null,
  id2   integer         not null
);
CREATE UNIQUE INDEX test31_relation_pk ON test31_relation ( id1, id2 );
