drop table test_table;

create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table_pk
   on test_table ( id );


drop table test_table2;

create table test_table2 (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table2_pk
   on test_table2 ( id );


drop table test_table3;

create table test_table3 (
  id      numeric(10,0) not null,
  value1  varchar(200)  not null,
  value2  varchar(200), 
  number  numeric(10,0)
);

create unique index test_table3_pk
   on test_table3 ( id );


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
  bday  timestamp without time zone null
);

create unique index test_pks_person_pk on test_pks_person( fname, lname );


drop table test_pks_employee;

create table test_pks_employee (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  start_date timestamp without time zone null
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


drop table test_master;

create table test_master (
  id       numeric(10,0)    not null,
  value1     varchar(200)  not null,
  group_id numeric(10,0)  null
);

create unique index test_master_pk
  on test_master ( id );


drop table test_detail;

create table test_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1      varchar(200)  not null
);

create unique index test_detail_pk
  on test_detail ( detail_id );


drop table test_detail2;

create table test_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail2_pk on test_detail2 ( detail2_id );


drop table test_detail3;

create table test_detail3
(
  detail3_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail3_pk on test_detail3 ( detail3_id );


drop table test_group;

create table test_group (
  id     numeric(10,0)  not null,
  value1  varchar(200)  not null
);

create unique index test_group_pk
   on test_group ( id );


drop table test_types;

create table test_types (
  id       numeric(10,0)  not null,
  tdt      timestamp without time zone           not null,
  ttm      timestamp without time zone           not null,
  int_val  integer        null,
  long_val bigint         null,
  char_val char(1)        null,
  bool_val char(1)        null,
  bool_is_method char(1)  null,
  int_date integer        null,
  str_time varchar(40)    null,
  num_date numeric(17,0)         null,
  float_val float         null,
  real_val real         null,
  blob_val oid       null,
  clob_val text       null,
  blob_val2 oid      null,
  clob_val2 text      null,
  date_str  timestamp without time zone      null,
  long_date numeric(20,0) null
);

create unique index test_types_pk
  on test_types ( id );



drop table list_types;

create table list_types (
  o_char  CHAR(100)         null,
  o_nchar NATIONAL CHAR(100)   null,
  o_varchar VARCHAR(20) null,
  o_varchar2 VARCHAR(20) null,
  o_clob text null,
  o_long oid null,
  o_number NUMERIC null,
  o_int   INT null,
  o_date timestamp without time zone null,
  o_raw   oid     null,
  o_blob  oid         null,
  o_bfile oid  null
);

drop table test_conv;

create table test_conv (
    id               int     not null,
    bool_byte        int     null,
    bool_short       int     null,
    bool_short_minus    int     null,
    bool_int         int     null,
    bool_int_minus      int     null,
    bool_bigdec      numeric null,
    bool_bigdec_minus   numeric null,
    byte_int         int     null,
    short_int        int     null,
    long_int         int     null,
    double_int       int     null,
    float_int        float   null,
    byte_bigdec      numeric null,
    short_bigdec     numeric null,
    int_bigdec       numeric null,
    float_bigdec     numeric null,
    double_bigdec    numeric null,
    short_string     varchar(20) null,
    byte_string      varchar(20) null,
    int_string       varchar(20) null,
    long_string      varchar(20) null,
    bigdec_string    varchar(20) null,
    float_string     varchar(20) null,
    double_string    varchar(20) null
);

create unique index test_conv_pk on test_conv( id );


drop table test_keygen;

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


drop sequence test_keygen_seq;

create sequence test_keygen_seq;


drop table test_uuid;

create table test_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
);

create unique index test_uuid_pk
  on test_uuid ( id );


drop table test_uuid_ext;

create table test_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
);

create unique index test_uuid_ext_pk on test_uuid_ext ( id );


drop table test_seqtable;

create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );


drop table test_persistent;

create table test_persistent (
  id       integer         not null,
  ctime    timestamp without time zone            not null,
  mtime    timestamp without time zone            null,
  value1    varchar(200)    not null,
  parent_id integer        null,
  group_id numeric(10,0)   not null
);

create unique index test_persistent_pk on test_persistent ( id );


drop table test_related;

create table test_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index test_related_pk on test_related ( id );


drop table test_identity;

--
-- Note that 7.2.1 requires the following sequence to be dropped,
-- where as 7.3 version of postgresql will drop it automatically
--

drop sequence test_identity_id_seq;

create table test_identity (
  id SERIAL,
  attr varchar(200) not null
);


drop table test_identity_ext;

create table test_identity_ext (
  id integer not null,
  ext varchar(200) not null
);

create unique index test_ident_ext_pk on test_identity_ext ( id );

drop table test_col;

create table test_col (
  id       integer         not null,
  dum    integer    null
);

create unique index test_col_pk on test_col( id );


drop table test_item;

create table test_item (
  iid       integer         not null,
  id      integer         null
);

create unique index test_item_pk on test_item( iid );


drop table test_oqlext;

create table test_oqlext (
  ident   integer         not null,
  ext     integer         not null
);

create unique index test_oqlext_pk on test_oqlext( ident );


drop table test_oqlext2;

create table test_oqlext2 (
  id      integer         not null,
  ext     integer         not null
);

create unique index test_oqlext2_pk on test_oqlext2( id );


drop table test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );
create index test_oqltag_fk2 on test_oqltag( id2 );


drop table test_pks_project;

create table test_pks_project (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id    int             not null,
  name  varchar(100)
);

create unique index test_pks_project_pk on test_pks_project( id );



drop table test_nton_a;

create table test_nton_a (
  id         varchar(20)    not null,
  status     integer        not null
);


drop table test_nton_b;

create table test_nton_b (
  id         varchar(20)    not null,
  status     integer        not null
);


drop table master;
drop table depend1;
drop table depend2;

create table depend1(
  id int not null primary key
);

create table master(
  depend1_id int ,
  id int not null primary key
);

create table depend2(
  master_id int,
  id int not null primary key
);

alter table master
	add constraint fk_master_depend1
	foreign key (depend1_id) references depend1(id);

alter table depend2
	add constraint fk_depend2_master
	foreign key (master_id) references master(id);

drop sequence circ_brother_seq;
drop sequence circ_sister_seq;

drop table circ_brother;
drop table circ_sister;

create sequence circ_brother_seq;
create sequence circ_sister_seq;

create table circ_brother (
	brother_id int not null,
	brother_sibling int,
	primary key (brother_id));

create table circ_sister (
	sister_id int not null,
	sister_sibling int,
	primary key (sister_id));
