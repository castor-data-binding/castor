
drop table   test_table;

create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table_pk
   on test_table ( id );


drop table test_group_person;

create table test_group_person (
  gid int         not null,
  pid int        not null 
);

create index test_group_person_p_pk on test_group_person ( pid );

create index test_group_person_g_pk on test_group_person ( gid );


drop table test_many_group;

create table test_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
);

create unique index test_many_group_pk on test_many_group ( gid );


drop table test_many_person;

create table test_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
);

create unique index test_many_person_pk on test_many_person ( pid );


drop table test_pks_person;

create table test_pks_person (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  bday  timestamp null
);

create unique index test_pks_person_pk on test_pks_person( fname, lname );


drop table test_pks_employee;

create table test_pks_employee (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  start_date timestamp null
);

create unique index test_pks_person_employee_pk on test_pks_employee( fname, lname );


drop table test_pks_payroll;

create table test_pks_payroll (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create unique index test_pks_payroll_fk on test_pks_payroll( fname, lname );

create unique index test_pks_payroll_pk on test_pks_payroll( id );


drop table test_pks_address;

create table test_pks_address (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  id int               not null,
  street varchar(30)    null,
  city  varchar(30)    null,
  state varchar(2)     null,
  zip varchar(6)       null
);

create unique index test_pks_address_pk on test_pks_address( id );


drop table test_pks_contract;

create table test_pks_contract (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(90)  null
);

create unique index test_pks_contract_fk on test_pks_contract( fname, lname );

create unique index test_pks_contract_pk on test_pks_contract( policy_no, contract_no );


drop table test_pks_category_contract;

create table test_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
);


drop table test_pks_category;

create table test_pks_category (
  id  int              not null,
  name varchar(20)     not null
);

create unique index test_pks_category_pk on test_pks_category( id );


drop table test_table_ex;

create table test_table_ex (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
);

create unique index test_table_ex_pk on test_table_ex ( id );


drop table test_table_extends;

create table test_table_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
);

create unique index test_table_extends_pk on test_table_extends ( id );


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


drop table   test_types;

create table test_types (
  id       fixed(10,0)  not null,
  tdt      timestamp           not null,
  ttm      timestamp           not null,
  int_val  integer        null,
  long_val fixed(18,0)  null,
  char_val char(1)        null,
  bool_val char(1)        null,
  int_date integer        null,
  str_time char(12)       null,
  num_date fixed(17,0)    null,
  blob_val long byte      null,
  clob_val long           null,
  blob_val2 long byte     null,
  clob_val2 long          null,
  date_str  timestamp     null
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


drop table list_types;

create table list_types (
  o_char  CHAR  null,
  o_nchar CHAR  null,
  o_varchar VARCHAR(20) null,
  o_varchar2 VARCHAR(20) null,
  o_clob LONG null,
  o_long LONG null,
  o_number FIXED null,
  o_int   INT null,
  o_date timestamp null,
  o_raw   CHAR (20) BYTE   null,
  o_blob  LONG BYTE     null,
  o_bfile LONG BYTE     null
);



drop table   test_seqtable;

create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );


drop sequence   test_keygen_seq;

create sequence test_keygen_seq;


drop table test_identity;

create table test_identity (
  id fixed(10,0) default serial,
  attr varchar(200) not null
);


drop table test_identity_ext;

create table test_identity_ext (
  id fixed(10,0) not null,
  ext varchar(200) not null
);

create unique index test_ident_ext_pk on test_identity_ext ( id );


drop table test_col;

create table test_col (
  id     integer         not null,
  dum    integer    null
);

create unique index test_col_pk on test_col( id );


drop table test_item;

create table test_item (
  iid     integer         not null,
  id      integer         not null
);

create unique index test_item_pk on test_item( iid );


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


drop table test_oqlext;

create table test_oqlext (
  ident   integer         not null,
  ext     integer         not null
);

create unique index test_oqlext_pk on test_oqlext( ident );
