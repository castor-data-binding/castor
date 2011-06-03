create table test39_entity (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
);

create unique index test39_entity_pk ON test39_entity(id);

CREATE OR REPLACE FUNCTION proc_check_permissions(userName varchar(200), groupName varchar(200)) RETURNS SETOF test39_entity AS 'SELECT id, value1, value2 FROM test39_entity WHERE (value1 = $1) or (value2 = $2);' LANGUAGE SQL;
