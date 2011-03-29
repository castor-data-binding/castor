create table test25_master (
  id        fixed(10,0)  not null,
  value1    varchar(200)   not null,
  group_id  fixed(10,0)  null
)
//
create unique index test25_master_pk on test25_master ( id )
//
create table test25_detail (
  detail_id  fixed(10,0)  not null,
  master_id  fixed(10,0)  not null,
  value1     varchar(200)   not null
)
//
create unique index test25_detail_pk on test25_detail ( detail_id )
//
create table test25_detail2 (
  detail2_id  fixed(10,0)  not null,
  detail_id   fixed(10,0)  not null,
  value1      varchar(200 )  not null
)
//

create unique index test25_detail2_pk on test25_detail2 ( detail2_id )
//
create table test25_detail3 (
  detail3_id  fixed(10,0)  not null,
  detail_id   fixed(10,0)  not null,
  value1      varchar(200 )  not null
)
//
create unique index test25_detail3_pk on test25_detail3 ( detail3_id )
//
create table test25_group (
  id      fixed(10,0)  not null,
  value1  varchar(200)   not null
)
//
create unique index test25_group_pk on test25_group ( id )
//