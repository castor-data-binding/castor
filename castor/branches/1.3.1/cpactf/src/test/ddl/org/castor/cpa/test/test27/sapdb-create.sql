create table test27_master (
  id        fixed(10,0)  not null,
  value1    varchar(200)   not null,
  group_id  fixed(10,0)  null
)
//
create unique index test27_master_pk on test27_master ( id )
//

create table test27_detail (
  detail_id  fixed(10,0)  not null,
  master_id  fixed(10,0)  not null,
  value1     varchar(200)   not null
)
//
create unique index test27_detail_pk on test27_detail ( detail_id )
//
create table test27_detail2 (
  detail2_id  fixed(10,0)  not null,
  detail_id   fixed(10,0)  not null,
  value1      varchar(200 )  not null
)
//
create unique index test27_detail2_pk on test27_detail2 ( detail2_id )
//
create table test27_detail3 (
  detail3_id  fixed(10,0)  not null,
  detail_id   fixed(10,0)  not null,
  value1      varchar(200 )  not null
)
//
create unique index test27_detail3_pk on test27_detail3 ( detail3_id )
//
create table test27_group (
  id      fixed(10,0)  not null,
  value1  varchar(200)   not null
)
//
create unique index test27_group_pk on test27_group ( id )
//