create table test18_persist (
  id        integer       not null,
  ctime     datetime      not null,
  mtime     datetime      null,
  value1    varchar(200)  not null,
  parent_id integer       null,
  group_id  numeric(10,0) not null
)
go

create unique index test18_persist_pk on test18_persist ( id )
go

create table test18_related (
  id          integer     not null,
  persist_id  integer     not null
)
go

create unique index test18_related_pk on test18_related ( id )
go

create table test18_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
)
go

create unique index test18_group_pk on test18_group ( id )
go


