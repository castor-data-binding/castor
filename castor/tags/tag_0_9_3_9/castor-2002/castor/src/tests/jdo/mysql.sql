create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table_pk
   on test_table ( id );


create table test_group_person (
  gid int         not null,
  pid int        not null 
);

create index test_group_person_p_pk on test_group_person ( pid );

create index test_group_person_g_pk on test_group_person ( gid );


create table test_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
);

create unique index test_many_group_pk on test_many_group ( gid );


create table test_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
);

create unique index test_many_person_pk on test_many_person ( pid );


create table test_pks_person (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  bday  datetime null
);

create unique index test_pks_person_pk on test_pks_person( fname, lname );


create table test_pks_employee (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  start_date datetime null
);

create unique index test_pks_person_employee_pk on test_pks_employee( fname, lname );


create table test_pks_payroll (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create unique index test_pks_payroll_fk on test_pks_payroll( fname, lname );

create unique index test_pks_payroll_pk on test_pks_payroll( id );


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


create table test_pks_contract (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(90)  null
);

create unique index test_pks_contract_fk on test_pks_contract( fname, lname );

create unique index test_pks_contract_pk on test_pks_contract( policy_no, contract_no );


create table test_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
);


create table test_pks_category (
  id  int              not null,
  name varchar(20)     not null
);

create unique index test_pks_category_pk on test_pks_category( id );


create table test_table_ex (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
);

create unique index test_table_ex_pk on test_table_ex ( id );


create table test_table_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
);

create unique index test_table_extends_pk on test_table_extends ( id );


create table test_race (
  id      int          not null,
  value1  int          not null
);

create unique index test_race_pk on test_race ( id );


create table test_master (
  id       numeric(10,0)    not null,
  value1     varchar(200)  not null,
  group_id numeric(10,0)  null
);

create unique index test_master_pk
  on test_master ( id );


create table test_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1      varchar(200)  not null
);

create unique index test_detail_pk
  on test_detail ( detail_id );


create table test_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail2_pk on test_detail2 ( detail2_id );


create table test_group (
  id     numeric(10,0)  not null,
  value1  varchar(200)  not null
);

create unique index test_group_pk
   on test_group ( id );


create table test_types (
  id       numeric(10,0)  not null,
  tdt      datetime           not null,
  ttm      datetime           not null,
  int_val  integer        null,
  long_val bigint         null,
  char_val char(1)        null,
  bool_val char(1)        null,
  int_date integer        null,
  str_time char(12)       null,
  num_date bigint         null,
  blob_val longblob       null,
  clob_val longtext       null,
  blob_val2 longblob      null,
  clob_val2 longtext      null,
  date_str  datetime      null
);

create unique index test_types_pk
  on test_types ( id );



create table list_types (
  o_char  CHAR(100)         null,
  o_nchar NATIONAL CHAR(100)   null,
  o_varchar VARCHAR(20) null,
  o_varchar2 VARCHAR(20) null,
  o_clob LONGTEXT null,
  o_long LONGBLOB null,
  o_number NUMERIC null,
  o_int   INT null,
  o_date datetime null,
  o_raw   TINYBLOB     null,
  o_blob  BLOB         null,
  o_bfile MEDIUMBLOB  null
);


create table test_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);

create unique index test_keygen_pk
  on test_keygen ( id );

create table test_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
);

create unique index test_keygen_ext_pk on test_keygen_ext ( id );

create table test_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
);

create unique index test_uuid_pk
  on test_uuid ( id );

create table test_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
);

create unique index test_uuid_ext_pk on test_uuid_ext ( id );


create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );


create table test_persistent (
  id       integer         not null,
  ctime    datetime            not null,
  mtime    datetime            null,
  value1    varchar(200)    not null,
  parent_id integer        null,
  group_id numeric(10,0)   not null
);

create unique index test_persistent_pk on test_persistent ( id );

create table test_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index test_related_pk on test_related ( id );

create table test_identity (
  id integer not null primary key auto_increment,
  attr varchar(200) not null
);

create table test_identity_ext (
  id integer not null,
  ext varchar(200) not null
);

create unique index test_ident_ext_pk on test_identity_ext ( id );

create table test_col (
  id       integer         not null,
  dum    integer    null
);

create unique index test_col_pk on test_col( id );

create table test_item (
  iid       integer         not null,
  id      integer         not null
);

create unique index test_item_pk on test_item( iid );


create table test_oqlext (
  ident   integer         not null,
  ext     integer         not null
);

create unique index test_oqlext_pk on test_oqlext( ident );

