create table test37_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
/
create unique index test37_entity_pk on test37_entity ( id )
/