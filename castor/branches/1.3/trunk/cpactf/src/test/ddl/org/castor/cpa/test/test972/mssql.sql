DROP TABLE test972_newprod
GO

DROP TABLE test972_prod
GO

DROP TABLE test972_prod_group
GO

CREATE TABLE test972_newprod (
  ID       numeric(22)    NOT NULL,
  NAME     varchar(200)   NOT NULL,
  PRICE    numeric(18,2)  NOT NULL,
  GROUP_ID numeric(22)    NOT NULL
)
GO

CREATE TABLE test972_prod (
  id        int           NOT NULL,
  name      varchar(200)  NOT NULL,
  price     numeric(18,2) NOT NULL,
  group_id  int           NOT NULL
)
GO

CREATE TABLE test972_prod_group (
  id    int          NOT NULL,
  name  varchar(200) NOT NULL
)
GO
