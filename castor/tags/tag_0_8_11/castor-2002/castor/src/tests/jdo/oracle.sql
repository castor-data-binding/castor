drop table   test_table;
create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);
create unique index test_table_pk
   on test_table ( id );
grant all on test_table to test;


-- test_table_ex
drop table test_table_ex;

create table test_table_ex (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
);

create unique index test_table_ex_pk on test_table_ex ( id );

grant all on test_table_ex to test;


-- test_race
drop table test_race;

create table test_race (
  id      int          not null,
  value1  int          not null
);

create unique index test_race_pk on test_race ( id );

grant all on test_race to test;


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


-- test_smaster
drop table test_smaster;
create table test_smaster (
  id       numeric(10,0)    not null
);
create unique index test_smaster_pk on test_smaster ( id );


-- test_sdetail
drop table test_sdetail;
create table test_sdetail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null
);
create unique index test_sdetail_pk on test_sdetail ( detail_id );
create index test_sdetail_fk on test_sdetail ( master_id );


drop table   test_types;
create table test_types (
  id       numeric(10,0)  not null,
  tdt      date           not null,
  ttm      date           not null,
  int_val  integer        null,
  long_val numeric(18,0)  null,
  char_val char(1)        null,
  bool_val char(1)        null,
  dbl_val  numeric(14,2)  not null,
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

CREATE OR REPLACE PACKAGE test AS
    TYPE TestCursor IS REF CURSOR RETURN test_table%ROWTYPE;
END test;
/

-- The test stored procedure on PL/SQL
CREATE OR REPLACE FUNCTION proc_check_permissions ( userName VARCHAR,
                                                    groupName VARCHAR)
    RETURN test.TestCursor AS
res test.TestCursor;
BEGIN
    OPEN res FOR SELECT id, value1, value2 FROM test_table WHERE value1 = userName
           UNION SELECT id, value1, value2 FROM test_table WHERE value2 = groupName;
    RETURN res;
END;
/
