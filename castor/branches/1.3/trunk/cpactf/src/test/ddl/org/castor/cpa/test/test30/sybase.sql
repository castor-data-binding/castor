DROP TABLE test30_extends
GO
DROP TABLE test30_entity
GO

CREATE TABLE test30_entity (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
GO
CREATE UNIQUE INDEX test30_entity_pk ON test30_entity ( id )
GO
GRANT ALL ON test30_entity TO test
GO

CREATE TABLE test30_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
)
GO
CREATE UNIQUE INDEX test30_extends_pk ON test30_extends ( id )
GO
GRANT ALL ON test30_extends TO test
GO

DROP TABLE test30_group
GO
CREATE TABLE test30_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
)
GO
CREATE UNIQUE INDEX test30_group_pk ON test30_group ( id )
GO
GRANT ALL ON test30_group TO test
GO
