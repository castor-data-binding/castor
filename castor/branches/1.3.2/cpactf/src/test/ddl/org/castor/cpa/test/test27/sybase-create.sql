create table test27_master (
  id        numeric(10,0)  not null,
  value1    varchar(200)   not null,
  group_id  numeric(10,0)  default null
)
go

create unique index test27_master_pk on test27_master ( id )
go

create table test27_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1     varchar(200)   not null
)
go

create unique index test27_detail_pk on test27_detail ( detail_id )
go

create table test27_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id   numeric(10,0)  not null,
  value1      varchar(200 )  not null
)
go

create unique index test27_detail2_pk on test27_detail2 ( detail2_id )
go

create table test27_detail3 (
  detail3_id  numeric(10,0)  not null,
  detail_id   numeric(10,0)  not null,
  value1      varchar(200 )  not null
)
go

create unique index test27_detail3_pk on test27_detail3 ( detail3_id )
go

create table test27_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
)
go

create unique index test27_group_pk on test27_group ( id )
go