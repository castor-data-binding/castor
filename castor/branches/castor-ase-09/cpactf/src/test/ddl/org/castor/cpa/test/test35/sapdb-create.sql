create table test35_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
//
create unique index test35_entity_pk on test35_entity ( id )
//