create table test38_entity (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index test38_entity_pk on test38_entity ( id )
go

create table test38_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
)
go
create unique index test38_extends_pk on test38_extends ( id )
go

create table test38_call (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index test38_call_pk on test38_call ( id )
go
