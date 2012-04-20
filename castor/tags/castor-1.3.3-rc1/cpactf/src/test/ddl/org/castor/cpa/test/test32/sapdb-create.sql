create table test32_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
//
create unique index test32_entity_pk on test32_entity ( id )
//