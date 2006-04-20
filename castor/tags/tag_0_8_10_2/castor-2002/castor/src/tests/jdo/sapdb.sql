drop table   test_table;

create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table_pk
   on test_table ( id );


drop table test_table_ex;

create table test_table_ex (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
);

create unique index test_table_ex_pk on test_table_ex ( id );


drop table test_race;

create table test_race (
  id      int          not null,
  value1  int          not null
);

create unique index test_race_pk on test_race ( id );


drop table   test_master;

create table test_master (
  id       fixed(10,0)    not null,
  value1     varchar(200)  not null,
  group_id fixed(10,0)  null
);

create unique index test_master_pk
  on test_master ( id );


drop table   test_detail;

create table test_detail (
  detail_id  fixed(10,0)  not null,
  master_id  fixed(10,0)  not null,
  value1      varchar(200)  not null
);

create unique index test_detail_pk
  on test_detail ( detail_id );


drop table test_detail2;

create table test_detail2 (
  detail2_id  fixed(10,0)  not null,
  detail_id  fixed(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail2_pk on test_detail2 ( detail2_id );


drop table   test_group;

create table test_group (
  id     fixed(10,0)  not null,
  value1  varchar(200)  not null
);

create unique index test_group_pk
   on test_group ( id );


drop table test_smaster;

create table test_smaster (
  id       fixed(10,0)    not null
);

create unique index test_smaster_pk on test_smaster ( id );


drop table test_sdetail;

create table test_sdetail (
  detail_id  fixed(10,0)  not null,
  master_id  fixed(10,0)  not null
);

create unique index test_sdetail_pk on test_sdetail ( detail_id );

create index test_sdetail_fk on test_sdetail ( master_id );


drop table   test_types;

create table test_types (
  id       fixed(10,0)  not null,
  tdt      timestamp           not null,
  ttm      timestamp           not null,
  int_val  integer        null,
  long_val fixed(18,0)  null,
  char_val char(1)        null,
  bool_val char(1)        null,
  dbl_val  fixed(14,2)  not null,
  int_date integer        null,
  str_time char(12)       null,
  num_date fixed(17,0)  null
);

create unique index test_types_pk
  on test_types ( id );


drop table   test_keygen;

create table test_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);

create unique index test_keygen_pk
  on test_keygen ( id );


drop table test_keygen_ext;

create table test_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
);

create unique index test_keygen_ext_pk on test_keygen_ext ( id );


drop table   test_seqtable;

create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );


drop sequence   test_keygen_seq;

create sequence test_keygen_seq;


drop table test_persistent;

create table test_persistent (
  id       integer         not null,
  ctime    timestamp            not null,
  mtime    timestamp            null,
  value1    varchar(200)    not null,
  parent_id integer        null,
  group_id fixed(10,0)   not null
);

create unique index test_persistent_pk on test_persistent ( id );

