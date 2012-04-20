create table test18_persist (
  id        integer       not null,
  ctime     timestamp     not null,
  mtime     timestamp,
  value1    varchar(200)  not null,
  parent_id integer,
  group_id  numeric(10,0) not null
);

create unique index test18_persist_pk on test18_persist ( id );

create table test18_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index test18_related_pk on test18_related ( id );

create table test18_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
);

create unique index test18_group_pk on test18_group ( id );
