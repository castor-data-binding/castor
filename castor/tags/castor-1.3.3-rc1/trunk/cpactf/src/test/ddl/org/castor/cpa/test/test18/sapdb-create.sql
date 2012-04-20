create table test18_persist (
  id        integer       not null,
  ctime     timestamp      not null,
  mtime     timestamp,
  value1    varchar(200)  not null,
  parent_id integer,
  group_id  numeric(10,0) not null,
  primary key (id)
)
//

create table test18_related (
  id          integer     not null,
  persist_id  integer     not null,
  primary key (id)
)
//

create table test18_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null,
  primary key (id)
)
//