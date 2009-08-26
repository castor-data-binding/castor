-- connect 'jdbc:derby://127.0.0.1/tests;create=true;user=test;password=test';

-- tc0x TESTS

drop table tc0x_sample;

create table tc0x_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc0x_sample_pk on tc0x_sample ( id );

drop table tc0x_race;

create table tc0x_race (
  id      int          not null,
  value1  int          not null
);

create unique index tc0x_race_pk on tc0x_race ( id );

-- UNDEFINED TESTS

drop table   test_table2;

create table test_table2 (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table2_pk on test_table2 ( id );

-- base class

drop table test_rel_person;

create table test_rel_person (
  pid    int             not null,
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date
);

create unique index test_rel_per_pk on test_rel_person( pid );

-- extend base class (person)

drop table test_rel_employee;

create table test_rel_employee (
  pid    int             not null,
  start_date date 
);

create unique index test_rel_empl_pk on test_rel_employee( pid );

-- depends class of person

drop table test_rel_address;

create table test_rel_address (
  pid    int             not null,
  id  int               not null,
  street varchar(30) ,
  city  varchar(30) ,
  state varchar(30) ,
  zip varchar(30) 
);

create index test_rel_addr_fk on test_rel_address( pid );

create unique index test_rel_addr_pk on test_rel_address( id );

-- depend class of employee

drop table test_rel_payroll;

create table test_rel_payroll (
  pid    int             not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create index test_rel_pay_fk on test_rel_payroll( pid );

create unique index test_rel_pay_pk on test_rel_payroll( id );

drop table   test_master;

create table test_master (
  id       numeric(10,0)    not null,
  value1    varchar(200)   not null,
  group_id numeric(10,0)  
);

create unique index test_master_pk on test_master ( id );

-- test_detail

drop table   test_detail;

create table test_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1      varchar(200)  not null
);

create unique index test_detail_pk on test_detail ( detail_id );

-- test_detail2

drop table test_detail2;

create table test_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail2_pk on test_detail2 ( detail2_id );

drop table test_detail3;

create table test_detail3 (
  detail3_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail3_pk on test_detail3 ( detail3_id );

-- test_keygen

drop table   test_keygen;

create table test_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);

create unique index test_keygen_pk on test_keygen ( id );

-- test_keygen_ext

drop table test_keygen_ext;

create table test_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
);

create unique index test_keygen_ext_pk on test_keygen_ext ( id );

drop table test_uuid;

create table test_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
);

create unique index test_uuid_pk on test_uuid ( id );

drop table test_uuid_ext;

create table test_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
);

create unique index test_uuid_ext_pk on test_uuid_ext ( id );

drop table   test_seqtable;

create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );

-- sequences not supported by Apache Derby
-- drop sequence   test_keygen_seq;
-- create sequence test_keygen_seq;
-- grant all on test_keygen_seq to test;

-- test the identity key generator

-- drop table test_identity;

-- create table test_identity (
--   id int not null generated always as identity,
--   attr varchar(200) not null
-- );

-- drop table test_identity_ext;

-- create table test_identity_ext (
--   id int not null generated always as identity,
--   ext varchar(200) not null
-- );

-- create unique index test_ident_ext_pk on test_identity_ext ( id );


-- list_types

drop table list_types;

create table list_types (
  o_char  CHAR(100),
  o_nchar VARCHAR(100)   ,
  o_varchar VARCHAR(20) ,
  o_varchar2 VARCHAR(20) ,
  o_clob CLOB ,
  o_long BLOB ,
  o_number NUMERIC ,
  o_int   INT ,
  o_date TIMESTAMP ,
  o_raw   BLOB     ,
  o_blob  BLOB         ,
  o_bfile BLOB  
);

drop table test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );

create index test_oqltag_fk2 on test_oqltag( id2 );

