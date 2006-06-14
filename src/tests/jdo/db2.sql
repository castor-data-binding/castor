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


-- tc1x TESTS

drop table tc1x_sample;

create table tc1x_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc1x_sample_pk on tc1x_sample ( id );

drop table tc1x_persist;

create table tc1x_persist (
  id         integer         not null,
  ctime      timestamp       not null,
  mtime      timestamp,
  value1     varchar(200)    not null,
  parent_id  integer,
  group_id   numeric(10,0)   not null
);

create unique index tc1x_persist_pk on tc1x_persist ( id );

drop table tc1x_related;

create table tc1x_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index tc1x_related_pk on tc1x_related ( id );

drop table tc1x_group;

create table tc1x_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
);

create unique index tc1x_group_pk on tc1x_group ( id );

drop table tc1x_handling;

create table tc1x_handling (
  id             numeric(10,0)  not null,
  tdt            timestamp      not null,
  ttm            timestamp      not null,
  int_val        integer,
  float_val      float,
  real_val       real,
  long_val       numeric(18,0),
  char_val       char(1),
  bool_val       char(1),
  bool_is_method char(1),
  int_date       integer,
  str_time       char(23),
  num_date       numeric(17,0),
  date_str       timestamp,
  long_date      numeric(20,0)
);

create unique index tc1x_handling_pk on tc1x_handling ( id );

drop table tc1x_lob;

create table tc1x_lob (
  id        numeric(10,0)  not null,
  blob_val  blob,
  clob_val  clob,
  blob_val2 blob,
  clob_val2 clob
);

create unique index tc1x_lob_pk on tc1x_lob ( id );

drop table tc1x_conv;

create table tc1x_conv (
    id                 int         not null,
    bool_byte          int,
    bool_short         int,
    bool_short_minus   int,
    bool_int           int,
    bool_int_minus     int,
    bool_bigdec        numeric,
    bool_bigdec_minus  numeric,
    byte_int           int         ,
    short_int          int         ,
    long_int           int         ,
    double_int         int         ,
    float_int          float       ,
    byte_bigdec        numeric     ,
    short_bigdec       numeric     ,
    int_bigdec         numeric     ,
    float_bigdec       numeric     ,
    double_bigdec      numeric     ,
    short_string       varchar(20) ,
    byte_string        varchar(20) ,
    int_string         varchar(20) ,
    long_string        varchar(20) ,
    bigdec_string      varchar(20) ,
    float_string       varchar(20) ,
    double_string      varchar(20) 
);

create unique index tc1x_conv_pk on tc1x_conv( id );

drop table tc1x_serial;

create table tc1x_serial (
  id      integer        not null,
  dep     blob           
);

create unique index tc1x_serial_pk on tc1x_serial( id );

drop table tc1x_rollback;

create table tc1x_rollback (
  id      numeric(10,0) not null,
  value1  varchar(200)  not null,
  value2  varchar(200),
  numb     numeric(10,0)
);

create unique index tc1x_rollback_pk on tc1x_rollback ( id );

drop table tc1x_pks_person;

create table tc1x_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date            
);

create unique index tc1x_pks_prs_pk on tc1x_pks_person( fname, lname );

drop table tc1x_pks_employee;

create table tc1x_pks_employee (
  fname      varchar(100)    not null,
  lname      varchar(100)    not null,
  start_date date            
);

create unique index tc1x_pks_emp_pk on tc1x_pks_employee( fname, lname );

drop table tc1x_pks_payroll;

create table tc1x_pks_payroll (
  fname       varchar(100)  not null,
  lname       varchar(100)  not null,
  id          int           not null,
  holiday     int           not null,
  hourly_rate int           not null
);

create unique index tc1x_pks_pay_fk on tc1x_pks_payroll( fname, lname );

create unique index tc1x_pks_pay_pk on tc1x_pks_payroll( id );

drop table tc1x_pks_address;

create table tc1x_pks_address (
  fname   varchar(100)  not null,
  lname   varchar(100)  not null,
  id      int           not null,
  street  varchar(30)   ,
  city    varchar(30)   ,
  state   varchar(2)    ,
  zip     varchar(6)    
);

create unique index tc1x_pks_add_pk on tc1x_pks_address( id );

drop table tc1x_pks_contract;

create table tc1x_pks_contract (
  fname        varchar(100)  not null,
  lname        varchar(100)  not null,
  policy_no    int           not null,
  contract_no  int           not null,
  c_comment    varchar(100)  
);

create unique index tc1x_pks_cont_fk on tc1x_pks_contract( fname, lname );

create unique index tc1x_pks_cont_pk on tc1x_pks_contract( policy_no, contract_no );

drop table tc1x_pks_category_contract;

create table tc1x_pks_category_contract (
  policy_no    int      not null,
  contract_no  int      not null,
  cate_id      int      not null
);

drop table tc1x_pks_category;

create table tc1x_pks_category (
  id    int              not null,
  name  varchar(100)     not null
);

create unique index tc1x_pks_cat_pk on tc1x_pks_category( id );


-- tc2x TESTS

drop table  tc2x_master;

create table tc2x_master (
  id        numeric(10,0)  not null,
  value1    varchar(200)   not null,
  group_id  numeric(10,0)  default null
);

create unique index tc2x_master_pk on tc2x_master ( id );

drop table  tc2x_detail;

create table tc2x_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1     varchar(200)   not null
);

create unique index tc2x_detail_pk on tc2x_detail ( detail_id );

drop table  tc2x_detail2;

create table tc2x_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id   numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index tc2x_detail2_pk on tc2x_detail2 ( detail2_id );

drop table  tc2x_detail3;

create table tc2x_detail3 (
  detail3_id  numeric(10,0)  not null,
  detail_id   numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index tc2x_detail3_pk on tc2x_detail3 ( detail3_id );

drop table  tc2x_group;

create table tc2x_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
);

create unique index tc2x_group_pk on tc2x_group ( id );

drop table tc2x_depend2;
drop table tc2x_depend_master;
drop table tc2x_depend1;

create table tc2x_depend1 (
  id          int not null primary key
);

create table tc2x_depend_master (
  id          int not null primary key,
  depend1_id  int,
  index tc2x_master_depend ( depend1_id ),
  foreign key ( depend1_id ) references tc2x_depend1 ( id )
);

create table tc2x_depend2 (
  id          int not null primary key,
  master_id   int,
  index tc2x_depend_master ( master_id ),
  foreign key ( master_id ) references tc2x_depend_master ( id )
);

drop table  tc2x_keygen;

create table tc2x_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);

create unique index tc2x_keygen_pk on tc2x_keygen ( id );

drop table  tc2x_keygen_ext;

create table tc2x_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
);

create unique index tc2x_keygen_ext_pk on tc2x_keygen_ext ( id );

drop table if exists tc2x_keygen_string;
create table tc2x_keygen_string (
  id    varchar(200)  not null,
  attr  varchar(200)  not null
);

create unique index tc2x_keygen_string_pk on tc2x_keygen_string ( id );

drop table if exists tc2x_keygen_ext_string;
create table tc2x_keygen_ext_string (
  id   varchar(200) not null,
  ext  varchar(200) not null
);

create unique index tc2x_keygen_ext_string_pk on tc2x_keygen_ext_string ( id );

drop table  tc2x_uuid;

create table tc2x_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
);

create unique index tc2x_uuid_pk on tc2x_uuid ( id );

drop table  tc2x_uuid_ext;

create table tc2x_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
);

create unique index tc2x_uuid_ext_pk on tc2x_uuid_ext ( id );

drop table  tc2x_identity;

create table tc2x_identity (
  id    integer not null primary key,
  attr  varchar(200) not null
);

drop table  tc2x_identity_ext;

create table tc2x_identity_ext (
  id   integer not null,
  ext  varchar(200) not null
);

create unique index tc2x_ident_ext_pk on tc2x_identity_ext ( id );

drop table  tc2x_seqtable;

create table tc2x_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index tc2x_seqtable_pk on tc2x_seqtable ( table_name );


-- tc3x TESTS

drop table tc3x_entity;

create table tc3x_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc3x_entity_pk on tc3x_entity ( id );

drop table tc3x_extends;

create table tc3x_extends (
  id      int          not null,
  value3  varchar(200) ,
  value4  varchar(200) 
);

create unique index tc3x_extends_pk on tc3x_extends ( id );

drop table tc3x_call;

create table tc3x_call (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) 
);

create unique index tc3x_call_pk on tc3x_call ( id );

drop table tc3x_persistent;

create table tc3x_persistent (
  id        integer         not null,
  ctime     timestamp       not null,
  mtime     timestamp       ,
  value1    varchar(200)    not null,
  parent_id integer         ,
  group_id  numeric(10,0)   not null
);

create unique index tc3x_persistent_pk on tc3x_persistent ( id );


drop table tc3x_related;

create table tc3x_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index tc3x_related_pk on tc3x_related ( id );

drop table tc3x_group;

create table tc3x_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)  not null
);

create unique index tc3x_group_pk on tc3x_group ( id );

drop table tc3x_extend1;

create table tc3x_extend1 (
  ident   integer         not null,
  ext     integer         not null
);

create unique index tc3x_extend1_pk on tc3x_extend1 ( ident );

drop table tc3x_extend2;

create table tc3x_extend2 (
  id      integer         not null,
  ext     integer         not null
);

create unique index tc3x_extend2_pk on tc3x_extend2 ( id );




-- UNDEFINED TESTS

-- tc7x_table
drop table   tc7x_table;

create table tc7x_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc7x_table_pk on tc7x_table ( id );

-- grant all on tc7x_table to test;


drop table   test_table2;

create table test_table2 (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table2_pk on test_table2 ( id );

-- grant all on test_table to test;


-- test many to many

drop table tc7x_group_person;
drop table tc7x_many_group;
drop table tc7x_many_person;

create table tc7x_many_group (
  gid       int           not null primary key,
  value1    varchar(100)  not null
);

-- create unique index test_mgroup_pk on tc7x_many_group ( gid );

-- grant all on tc7x_many_group to test;



create table tc7x_many_person (
   pid      int          not null primary key,
   value1   varchar(100) not null,
   helloworld varchar(100) ,
   sthelse varchar(100) 
);

-- create unique index test_mperson_pk on tc7x_many_person ( pid );

-- grant all on tc7x_many_person to test;



create table tc7x_group_person (
  gid int         not null,
  pid int        not null,
  CONSTRAINT person_delete
    FOREIGN KEY(pid) 
    REFERENCES tc7x_many_person(pid),
  CONSTRAINT group_delete
    FOREIGN KEY(gid) 
    REFERENCES tc7x_many_group(gid)
);

create index test_gperson_p_pk on tc7x_group_person ( pid );

create index test_gperson_g_pk on tc7x_group_person ( gid );

-- grant all on tc7x_group_person to test;

drop table if exists tc7x_as_main;
drop table if exists tc7x_as_assoc1;

create table tc7x_as_assoc1 (
  id        int not null,
  name      varchar(200) not null
);

insert into tc7x_as_assoc1 (id, name) values (1, 'assoc1');

create table tc7x_as_main (
  id        int not null,
  name      varchar(200) not null,
  assoc1_id	int default null
);

insert into tc7x_as_main (id, name, assoc1_id) values (1, 'main', 1);

drop table if exists tc7x_as_assoc_many;
drop table if exists tc7x_as_main_many;
create table tc7x_as_main_many (
  id        int not null,
  name      varchar(200) not null
);

insert into tc7x_as_main_many (id, name) values (1, 'main');

create table tc7x_as_assoc_many (
  id        int not null,
  name      varchar(200) not null,
  main_id	int
);

insert into tc7x_as_assoc_many (id, name, main_id) values (1, 'assoc.many.1', 1);
insert into tc7x_as_assoc_many (id, name, main_id) values (2, 'assoc.many.2', 1);



-- test multiple pk
drop table tc8x_pks_person;

create table tc8x_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date 
);

create unique index tc8x_pks_per_pk on tc8x_pks_person( fname, lname );

-- grant all on tc8x_pks_person to test;


drop table tc8x_pks_employee;

create table tc8x_pks_employee (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  start_date date 
);

create unique index t_pks_per_emp_pk on tc8x_pks_employee( fname, lname );

-- grant all on tc8x_pks_employee to test;


drop table tc8x_pks_payroll;

create table tc8x_pks_payroll (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create unique index tc8x_pks_pay_fk on tc8x_pks_payroll( fname, lname );

create unique index tc8x_pks_pay_pk on tc8x_pks_payroll( id );

-- grant all on tc8x_pks_payroll to test;


drop table tc8x_pks_project;

create table tc8x_pks_project (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id    int             not null,
  name  varchar(100)
);

create unique index tc8x_pks_pro_pk on tc8x_pks_project( id );

-- grant all on tc8x_pks_payroll to test;


drop table tc8x_pks_address;

create table tc8x_pks_address (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  street varchar(30)    ,
  city  varchar(30)    ,
  state varchar(2)     ,
  zip varchar(6)       
);

create unique index tc8x_pks_add_pk on tc8x_pks_address( id );

-- grant all on tc8x_pks_add to test;


drop table tc8x_pks_contract;

create table tc8x_pks_contract (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(100)  
);

create unique index tc8x_pks_cont_fk on tc8x_pks_contract( fname, lname );

create unique index tc8x_pks_cont_pk on tc8x_pks_contract( policy_no, contract_no );

-- grant all on tc8x_pks_cont to test;


drop table tc8x_pks_category_contract;

create table tc8x_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
);

-- grant all on tc8x_pks_category_contract to test;


drop table tc8x_pks_category;

create table tc8x_pks_category (
  id  int              not null,
  name varchar(100)     not null
);

create unique index tc8x_pks_cat_pk on tc8x_pks_category( id );

-- grant all on tc8x_pks_category to test;


-- base class
drop table test_rel_person;

create table test_rel_person (
  pid    int             not null,
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date
);

create unique index test_rel_per_pk on test_rel_person( pid );

-- grant all on test_rel_person to test;


-- extend base class (person)
drop table test_rel_employee;

create table test_rel_employee (
  pid    int             not null,
  start_date date 
);

create unique index test_rel_empl_pk on test_rel_employee( pid );

-- grant all on test_rel_employee to test;


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

-- grant all on test_rel_address to test;


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

-- grant all on test_rel_payroll to test;
-- end for test_relations


drop table   test_master;

create table test_master (
  id       numeric(10,0)    not null,
  value1    varchar(200)   not null,
  group_id numeric(10,0)  
);

create unique index test_master_pk
  on test_master ( id );

-- grant all on test_master to test;


-- test_detail
drop table   test_detail;

create table test_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1      varchar(200)  not null
);

create unique index test_detail_pk
  on test_detail ( detail_id );

-- grant all on test_detail to test;


-- test_detail2
drop table test_detail2;

create table test_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail2_pk on test_detail2 ( detail2_id );

-- grant all on test_detail2 to test;

drop table test_detail3;

create table test_detail3
(
  detail3_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index test_detail3_pk on test_detail3 ( detail3_id );

-- grant all on test_detail3 to test;


-- test_keygen
drop table   test_keygen;

create table test_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);

create unique index test_keygen_pk
  on test_keygen ( id );

-- grant all on test_keygen to test;


-- test_keygen_ext
drop table test_keygen_ext;

create table test_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
);

create unique index test_keygen_ext_pk on test_keygen_ext ( id );

-- grant all on test_keygen_ext to test;


drop table test_uuid;

create table test_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
);

create unique index test_uuid_pk on test_uuid ( id );

-- grant all on test_uuid to test;


drop table test_uuid_ext;

create table test_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
);

create unique index test_uuid_ext_pk on test_uuid_ext ( id );

-- grant all on test_uuid_ext to test;


drop table   test_seqtable;

create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );

-- grant all on test_seqtable to test;


drop sequence   test_keygen_seq;

create sequence test_keygen_seq;

-- grant all on test_keygen_seq to test;


-- test the identity key generator
drop table test_identity;

create table test_identity (
  id numeric(10,0) not null,
  attr varchar(200) not null
);

-- grant all on test_identity to test;


drop table test_identity_ext;

create table test_identity_ext (
  id numeric(10,0) not null,
  ext varchar(200) not null
);

create unique index test_ident_ext_pk on test_identity_ext ( id );

-- grant all on test_identity_ext to test;


-- tc7x_col
drop table tc7x_col;

create table tc7x_col (
  id       integer         not null,
  dum    integer    
);

create unique index tc7x_col_pk on tc7x_col( id );

-- grant all on tc7x_col to test;


drop table tc7x_item;

create table tc7x_item (
  iid       integer         not null,
  id      integer           
);

create unique index tc7x_item_pk on tc7x_item( iid );

-- grant all on tc7x_item to test;

drop table tc7x_comp_item;

create table tc7x_comp_item (
  iid       integer         not null,
  id      integer         not null
);

create unique index tc7x_comp_item_pk on tc7x_comp_item( iid );

-- grant all on tc7x_comp_item to test;


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

-- grant all on list_types to test;


drop table test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );

create index test_oqltag_fk2 on test_oqltag( id2 );

-- grant all on test_oqltag to test;


drop table tc8x_nton_a;
create table tc8x_nton_a (
  id         varchar(20)      not null,
  status     int              not null
);

-- grant all on tc8x_nton_a to test;

drop table tc8x_nton_b;
create table tc8x_nton_b (
  id         varchar(20)      not null,
  status     int              not null
);
-- grant all on tc8x_nton_b to test;


drop table tc7x_master;
drop table tc7x_depend1;
drop table tc7x_depend2;

create table tc7x_depend1(
  id int not null,
  constraint pk_depend1 primary key (id)
);

-- grant all on tc7x_depend1 to test;

create table tc7x_master(
  depend1_id int,
  id int not null,
  constraint pk_master primary key (id)
);

-- grant all on tc7x_master to test;


create table tc7x_depend2(
  master_id int,
  id int not null,
  constraint pk_depend2 primary key (id)
);

-- grant all on tc7x_depend2 to test;

alter table tc7x_master
	add constraint fk_master_depend1
	foreign key (depend1_id) references tc7x_depend1(id);

alter table tc7x_depend2
	add constraint fk_depend2_master
	foreign key (master_id) references tc7x_master(id);

drop table tc8x_circ_brother;
drop table tc8x_circ_sister;

create table tc8x_circ_brother (
	brother_id int not null,
	brother_sibling int,
	constraint pk_brother primary key (brother_id));

create table tc8x_circ_sister (
	sister_id int not null,
	sister_sibling int,
	constraint pk_sister primary key (sister_id));

-- tc166.TestLazy1to1
drop table tc8x_lazy_11_chd;
create table tc8x_lazy_11_chd (
  id        int not null,
  descr     varchar(20) not null
);

drop table tc8x_lazy_11_par;
create table tc8x_lazy_11_par (
  id        int not null,
  descr     varchar(20) not null,
  child_id  int
);

drop table tc8x_lazy_11_author;
create table tc8x_lazy_11_author (
  id			int not null,
  first_name		varchar(100) not null,
  last_name		varchar(100) not null
);

drop table tc8x_lazy_11_book;
create table tc8x_lazy_11_book (
  id			int not null,
  name 			varchar(100) not null,
  author_id		int not null
);

insert into tc8x_lazy_11_chd (id, descr) values (1, 'child 1');
insert into tc8x_lazy_11_chd (id, descr) values (2, 'child 2');
insert into tc8x_lazy_11_chd (id, descr) values (3, 'child 3');
insert into tc8x_lazy_11_chd (id, descr) values (4, 'child 4');

insert into tc8x_lazy_11_par (id, descr, child_id) values (1, 'parent 1', 1);
insert into tc8x_lazy_11_par (id, descr, child_id) values (2, 'parent 2', 2);
insert into tc8x_lazy_11_par (id, descr, child_id) values (3, 'parent 3', 1);
insert into tc8x_lazy_11_par (id, descr, child_id) values (5, 'parent 5', null);

insert into tc8x_lazy_11_author (id, first_name, last_name) values (1, 'Joe', 'Writer');
 
insert into tc8x_lazy_11_book (id, name, author_id) select 1, 'test book', tc8x_lazy_11_author.id from tc8x_lazy_11_author;
	
drop table tc8x_enum_prod;
create table tc8x_enum_prod (
  id        int not null,
  name      varchar(200) not null,
  kind      varchar(200) not null
);





-- tc8x

drop table tc8x_self_refer_parent;
create table tc8x_self_refer_parent (
  id        int not null,
  fid		int,
  name      varchar(200) not null
);

insert into tc8x_self_refer_parent (id, fid, name) values (1, null, 'entity1');
insert into tc8x_self_refer_parent (id, fid, name) values (2, 1, 'entity2');
insert into tc8x_self_refer_parent (id, fid, name) values (3, 1, 'entity3');

drop table tc8x_self_refer_child;
create table tc8x_self_refer_child (
  id        int not null,
  descr     varchar(200) not null
);

insert into tc8x_self_refer_child (id, descr) values (1, 'description1');
insert into tc8x_self_refer_child (id, descr) values (2, 'description2');
insert into tc8x_self_refer_child (id, descr) values (3, 'description3');

DROP TABLE tc8x_test_depends_ns;
CREATE TABLE tc8x_test_depends_ns (
  id int(11) NOT NULL,
  master_id int(11) NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  constraint prim_id primary key (id)
);

DROP TABLE tc8x_test_master_ns;
CREATE TABLE tc8x_test_master_ns (
  id int(11) NOT NULL,
  descrip varchar(50) NOT NULL default '',
  constraint prim_id primary key (id)
);

DROP TABLE tc8x_test_depends_ns_nokg;
CREATE TABLE tc8x_test_depends_ns_nokg (
  id int(11) NOT NULL,
  master_id int(11) NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  constraint prim_id primary key (id)
);

DROP TABLE tc8x_test_master_ns_nokg;
CREATE TABLE tc8x_test_master_ns_nokg (
  id int(11) NOT NULL,
  descrip varchar(50) NOT NULL default '',
  constraint prim_id primary key (id)
);

drop table tc8x_parent;
create table tc8x_parent (
  id        int not null PRIMARY KEY,
  name      varchar(200) not null
);

insert into tc8x_parent (id, name) values (1, 'entity1');

drop table tc8x_child;
create table tc8x_child (
  id        int not null PRIMARY KEY,
  descr     varchar(200) not null
);

insert into tc8x_child (id, descr) values (1, 'child1');

drop table tc8x_parent_compound;
create table tc8x_parent_compound (
  id1       int not null,
  id2       int not null,
  name      varchar(200) not null,
  PRIMARY KEY (id1, id2)
);

insert into tc8x_parent_compound (id1, id2, name) values (1, 1, 'entityCompound1');

drop table tc8x_child_compound;
create table tc8x_child_compound (
  id1       int not null,
  id2       int not null,
  descr     varchar(200) not null,
  PRIMARY KEY (id1, id2)
);

insert into tc8x_child_compound (id1, id2, descr) values (1, 1, 'childCompound1');


-- tc9x TESTS

drop table  tc9x_poly_ordr;
create table tc9x_poly_ordr (
  id int not null,
  name varchar (20) not null
);

drop table  tc9x_poly_detail;
create table tc9x_poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
);

drop table  tc9x_poly_owner;
create table tc9x_poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
);

drop table  tc9x_poly_prod;
create table tc9x_poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail	int not null,
  owner		int
);

drop table  tc9x_poly_computer;
create table tc9x_poly_computer (
  id   int not null,
  cpu  varchar(200) not null
);

drop table  tc9x_poly_laptop;
create table tc9x_poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
);

drop table  tc9x_poly_server;
create table tc9x_poly_server (
  id   int not null,
  numberOfCPUs  int not null,
  support int not null
);

drop table  tc9x_poly_car;
create table tc9x_poly_car (
  id   int not null,
  kw   int not null,
  make  varchar(200) not null
);

drop table  tc9x_poly_truck;
create table tc9x_poly_truck (
  id   int not null,
  max_weight   int not null
);

drop table  tc9x_poly_prod_multi;
create table tc9x_poly_prod_multi (
  id1        int not null,
  id2        int not null,
  name      varchar(200) not null,
  detail	int not null
);

drop table  tc9x_poly_computer_multi;
create table tc9x_poly_computer_multi (
  id1   int not null,
  id2        int not null,
  cpu  varchar(200) not null
);

drop table  tc9x_poly_laptop_multi;
create table tc9x_poly_laptop_multi (
  id1   int not null,
  id2        int not null,
  weight  int not null,
  resolution varchar(19) not null
);

drop table  tc9x_poly_server_multi;
create table tc9x_poly_server_multi (
  id1   int not null,
  id2        int not null,
  numberOfCPUs  int not null,
  support int not null
);

drop table  tc9x_poly_order_product;
create table tc9x_poly_order_product (
  order_id	int not null,
  product_id int not null
);

drop table  tc9x_poly_table_m;
create table tc9x_poly_table_m (
  id	int not null,
  name	varchar(20) not null
);

drop table  tc9x_poly_table_n;
create table tc9x_poly_table_n (
  id	int not null,
  name	varchar(20) not null
);

drop table  tc9x_poly_m_n;
create table tc9x_poly_m_n (
  m_id	int not null,
  n_id int not null
);


insert into tc9x_poly_detail (id, category, location) values (1, 'category 1', 'location 1');
insert into tc9x_poly_detail (id, category, location) values (2, 'category 2', 'location 2');
insert into tc9x_poly_detail (id, category, location) values (3, 'category 3', 'location 3');
insert into tc9x_poly_detail (id, category, location) values (4, 'category 4', 'location 4');
insert into tc9x_poly_detail (id, category, location) values (5, 'category 5', 'location 5');

insert into tc9x_poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1);
insert into tc9x_poly_computer (id, cpu) values (1, 'centrino');
insert into tc9x_poly_laptop (id, weight, resolution) values (1, 2800, '1280');

insert into tc9x_poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2);
insert into tc9x_poly_computer (id, cpu) values (2, 'centrino');
insert into tc9x_poly_laptop (id, weight, resolution) values (2, 2700, '1024');

insert into tc9x_poly_prod (id, name, detail, owner) values (3, 'server 3', 3, 3);
insert into tc9x_poly_computer (id, cpu) values (3, 'pentium 4');
insert into tc9x_poly_server (id, numberOfCPUs, support) values (3, 4, 3);

insert into tc9x_poly_prod (id, name, detail, owner) values (4, 'server 4', 4, 4);
insert into tc9x_poly_computer (id, cpu) values (4, 'pentium 4');
insert into tc9x_poly_server (id, numberOfCPUs, support) values (4, 16,5);

insert into tc9x_poly_prod (id, name, detail, owner) values (5, 'truck 5', 5, 5);
insert into tc9x_poly_car (id, kw, make) values (5, 60, 'make 5');
insert into tc9x_poly_truck (id, max_weight) values (5, 4);

insert into tc9x_poly_prod_multi (id1, id2, name, detail) values (1, 1, 'laptop 1', 1);
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (1, 1, 'centrino');
insert into tc9x_poly_laptop_multi (id1, id2, weight, resolution) values (1, 1, 2800, '1280');

insert into tc9x_poly_prod_multi (id1, id2, name, detail) values (2, 2, 'laptop 2', 2);
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (2, 2, 'centrino');
insert into tc9x_poly_laptop_multi (id1, id2, weight, resolution) values (2, 2, 2700, '1024');

insert into tc9x_poly_prod_multi (id1, id2, name, detail) values (3, 3, 'server 3', 3);
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (3, 3, 'pentium 4');
insert into tc9x_poly_server_multi (id1,  id2, numberOfCPUs, support) values (3, 3, 4, 3);

insert into tc9x_poly_prod_multi (id1, id2, name, detail) values (4, 4, 'server 4', 4);
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (4, 4, 'pentium 4');
insert into tc9x_poly_server_multi (id1, id2, numberOfCPUs, support) values (4, 4, 16,5);

insert into tc9x_poly_owner (id, name, product) values (1, 'owner 1', 1);
insert into tc9x_poly_owner (id, name, product) values (2, 'owner 2', 2);
insert into tc9x_poly_owner (id, name, product) values (3, 'owner 3', 3);
insert into tc9x_poly_owner (id, name, product) values (4, 'owner 4', 4);
insert into tc9x_poly_owner (id, name, product) values (5, 'owner 5', 5);

insert into tc9x_poly_ordr (id, name) values (1, 'order 1');

insert into tc9x_poly_order_product (order_id, product_id) values (1, 1);
insert into tc9x_poly_order_product (order_id, product_id) values (1, 2);

insert into tc9x_poly_m_n (m_id, n_id) values (1, 1);
insert into tc9x_poly_m_n (m_id, n_id) values (1, 2);

insert into tc9x_poly_table_m (id, name) values (1, 'm1');
insert into tc9x_poly_table_m (id, name) values (2, 'm2');

insert into tc9x_poly_table_n (id, name) values (1, 'n1');
insert into tc9x_poly_table_n (id, name) values (2, 'n2');

DROP TABLE  tc9x_poly_base;
CREATE TABLE tc9x_poly_base (
  id varchar(64) NOT NULL default '',
  color varchar(64) default NULL,
  PRIMARY KEY  (ID)
) ;

INSERT INTO tc9x_poly_base VALUES ('100','red');

DROP TABLE  tc9x_poly_derived;
CREATE TABLE tc9x_poly_derived (
  id varchar(64) NOT NULL default '',
  scent varchar(64) default NULL,
  PRIMARY KEY  (ID)
) ;
INSERT INTO tc9x_poly_derived VALUES ('100','vanilla');


DROP TABLE  tc9x_poly_container;
CREATE TABLE tc9x_poly_container (
  id varchar(64) NOT NULL default '',
  reference varchar(64) default NULL,
  PRIMARY KEY  (ID)
) ;
INSERT INTO tc9x_poly_container VALUES ('200','100');

DROP TABLE  tc9x_poly_Product;
CREATE TABLE tc9x_poly_Product(
  IdProd NUMERIC(10) not null PRIMARY KEY,
  NameProd   VARCHAR(30) default NULL,
  DescProd   VARCHAR(30) default NULL);

DROP TABLE  tc9x_poly_ActProduct;
CREATE TABLE tc9x_poly_ActProduct(
  IdAct NUMERIC(10) not null PRIMARY KEY REFERENCES tc9x_poly_Product (IdProd),
  BestSeason VARCHAR(30) default NULL);

DROP TABLE  tc9x_poly_ComposedOffer;
CREATE TABLE tc9x_poly_ComposedOffer(
  IdCOffer NUMERIC(10) not null PRIMARY KEY REFERENCES tc9x_poly_Product (IdProd),
  NameCO   VARCHAR(30) default NULL,
  DescCO   VARCHAR(30) default NULL);

DROP TABLE  tc9x_poly_OfferComposition;
CREATE TABLE tc9x_poly_OfferComposition(
  Offer NUMERIC(10) not null,
  Product NUMERIC(10) not null, 
  CONSTRAINT unique_rel UNIQUE (Offer, Product) );

-- tables required for TestPolymorphismDependedndObject

DROP TABLE tc9x_poly_extend_object;
CREATE TABLE tc9x_poly_extend_object (
  id            int NOT NULL default '0',
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO tc9x_poly_extend_object VALUES (1, 'This is the extended object.');

DROP TABLE tc9x_poly_base_object;
CREATE TABLE tc9x_poly_base_object (
  id           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
);

INSERT INTO tc9x_poly_base_object VALUES (1, 'This is the test object.', '0');

DROP TABLE tc9x_poly_depend_object;
CREATE TABLE tc9x_poly_depend_object (
  id           int NOT NULL default '0',
  parentId           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO tc9x_poly_depend_object VALUES(1, 1, 'This is a description');




# TC129 

DROP TABLE tc7x_container;
CREATE TABLE tc7x_container (
  id int NOT NULL ,
  name varchar(200) NULL,
  prop int default NULL,
  PRIMARY KEY (id)
);

INSERT INTO tc7x_container (id, name, prop) VALUES 
  (1,'Container 1',1),
  (2,'Container 2',2),
  (3,'Container 3',3),
  (4,'Container 4',4);

DROP TABLE tc7x_container_item;
CREATE TABLE tc7x_container_item (
  id int NOT NULL,
  item int default DEFAULT NULL,
  value varchar(200) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO tc7x_container_item (id, item, value) VALUES 
  (1,1,'Container item 1'),
  (2,2,'Container item 2'),
  (3,3,'Container item 3'),
  (4,4,'Container item 4'),
  (5,1,'Container item 5'),
  (6,2,'Container item 6'),
  (7,3,'Container item 7'),
  (8,4,'Container item 8');



# TC128a

drop table tc7x_sorted_container;
create table tc7x_sorted_container (
  id        int not null,
  name      varchar(200) not null
);

drop table tc7x_sorted_item;
create table tc7x_sorted_item(
  id        int not null,
  id_1		int not null,
  name      varchar(200) not null
);

insert into tc7x_sorted_container(id, name) values (1, 'container 1');
insert into tc7x_sorted_container(id, name) values (2, 'container 2');
insert into tc7x_sorted_container(id, name) values (1, 'container 3');

insert into tc7x_sorted_item (id, id_1, name) values (1, 1, 'container item 1');
insert into tc7x_sorted_item (id, id_1, name) values (2, 1, 'container item 2');
insert into tc7x_sorted_item (id, id_1, name) values (3, 2, 'container item 3');

# TC20x - self-referential relations 
 
drop table tc200_self_relation_folder;
create table tc200_self_relation_folder (
  id          int		     	not null,  
  name        varchar(255)    	not null,
  parent_id	  int				DEFAULT null
  
);

drop table tc200_self_relation_folder_parent;
create table tc200_self_relation_folder_parent (
  id          int		     	not null,  
  name        varchar(255)    	not null
);

drop table tc200_self_relation_folder_extend;
create table tc200_self_relation_folder_extend (
  id          int		     	not null,  
  parent_id	  int				DEFAULT null
);
