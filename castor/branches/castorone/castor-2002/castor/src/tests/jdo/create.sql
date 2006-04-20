drop table   test_table;
create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);
create unique index test_table_pk
   on test_table ( id );
grant all on test_table to test;


drop table   test_master;
create table test_master (
  id       numeric(10,0)    not null,
  value1     varchar(200)  not null,
  group_id numeric(10,0)  null
);
create unique index test_master_pk
  on test_master ( id );
grant all on test_master to test;


drop table   test_detail;
create table test_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1      varchar(200)  not null
);
create unique index test_detail_pk
  on test_detail ( detail_id );
grant all on test_detail to test;


-- test_detail2
drop table test_detail2;
create table test_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);
create unique index test_detail2_pk on test_detail2 ( detail2_id );
grant all on test_detail2 to test;


drop table   test_group;
create table test_group (
  id     numeric(10,0)  not null,
  value1  varchar(200)  not null
);
create unique index test_group_pk
   on test_group ( id );
grant all on test_group to test;


drop table   test_types;
create table test_types (
  id       numeric(10,0)  not null,
  tdt      datetime       not null,
  ttm      time           not null,
  int_val  integer        null,
  long_val numeric(18,0)  null,
  char_val char(1)        null,
  bool_val char(1)        null,
  int_date integer        null,
  str_time char(12)       null,
  num_date numeric(17,0)  null
);
create unique index test_types_pk
  on test_types ( id );
grant all on test_types to test;


drop table   test_keygen;
create table test_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);
create unique index test_keygen_pk
  on test_keygen ( id );
grant all on test_keygen to test;


-- test_keygen_ext
drop table test_keygen_ext;
create table test_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
);
create unique index test_keygen_ext_pk on test_keygen_ext ( id );
grant all on test_keygen_ext to test;


drop table   test_seqtable;
create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);
create unique index test_seqtable_pk
  on test_seqtable ( table_name );
grant all on test_seqtable to test;


drop sequence   test_keygen_seq;
create sequence test_keygen_seq;
grant all on test_keygen_seq to test;


-- test_persistent
drop table test_persistent;
create table test_persistent (
  id       integer         not null,
  ctime    date            not null,
  mtime    date            null,
  value1    varchar(200)    not null,
  parent_id integer        null,
  group_id numeric(10,0)   not null
);
create unique index test_persistent_pk on test_persistent ( id );

-- The test stored procedure on PL/pgSQL
drop function proc_check_permissions ( varchar, varchar);

create function proc_check_permissions ( varchar, varchar)
returns setof test_table as '
select id, value1, value2 from test_table where value1 = $1
union
select id, value1, value2 from test_table where value2 = $2;
' language 'sql';
