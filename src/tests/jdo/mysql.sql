# NOTE: to make all the tests for mysql pass, you need InnoDB-support:
# http://dev.mysql.com/doc/mysql/en/InnoDB_in_MySQL_3.23.html

-- tc0x TESTS

drop table if exists tc0x_sample;

create table tc0x_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc0x_sample_pk on tc0x_sample ( id );

drop table if exists tc0x_race;

create table tc0x_race (
  id      int          not null,
  value1  int          not null
);

create unique index tc0x_race_pk on tc0x_race ( id );


-- tc1x TESTS

drop table if exists tc1x_sample;

create table tc1x_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc1x_sample_pk on tc1x_sample ( id );

drop table if exists tc1x_persist;

create table tc1x_persist (
  id        integer       not null,
  ctime     datetime      not null,
  mtime     datetime      null,
  value1    varchar(200)  not null,
  parent_id integer       null,
  group_id  numeric(10,0) not null
);

create unique index tc1x_persist_pk on tc1x_persist ( id );

drop table if exists tc1x_related;

create table tc1x_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index tc1x_related_pk on tc1x_related ( id );

drop table if exists tc1x_group;

create table tc1x_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
);

create unique index tc1x_group_pk on tc1x_group ( id );

drop table if exists tc1x_handling;

create table tc1x_handling (
  id             numeric(10,0)  not null,
  tdt            datetime       not null,
  ttm            datetime       not null,
  int_val        integer        null,
  float_val      float          null, 
  real_val       real           null,
  long_val       bigint         null,
  char_val       char(1)        null,
  bool_val       char(1)        null,
  bool_is_method char(1)        null,
  int_date       integer        null,
  str_time       char(23)       null,
  num_date       bigint         null,
  date_str       datetime       null,
  long_date      numeric(20,0)  null
);

create unique index tc1x_handling_pk on tc1x_handling ( id );

drop table if exists tc1x_lob;

create table tc1x_lob (
  id        numeric(10,0)  not null,
  blob_val  longblob           null,
  clob_val  longtext           null,
  blob_val2 longblob           null,
  clob_val2 longtext           null
);

create unique index tc1x_lob_pk on tc1x_lob ( id );

drop table if exists tc1x_conv;

create table tc1x_conv (
    id                 int         not null,
    bool_byte          int         null,
    bool_short         int         null,
    bool_short_minus   int         null,
    bool_int           int         null,
    bool_int_minus     int         null,
    bool_bigdec        numeric     null,
    bool_bigdec_minus  numeric     null,
    byte_int           int         null,
    short_int          int         null,
    long_int           int         null,
    double_int         int         null,
    float_int          float       null,
    byte_bigdec        numeric     null,
    short_bigdec       numeric     null,
    int_bigdec         numeric     null,
    float_bigdec       numeric     null,
    double_bigdec      numeric     null,
    short_string       varchar(20) null,
    byte_string        varchar(20) null,
    int_string         varchar(20) null,
    long_string        varchar(20) null,
    bigdec_string      varchar(20) null,
    float_string       varchar(20) null,
    double_string      varchar(20) null
);

create unique index tc1x_conv_pk on tc1x_conv( id );

drop table if exists tc1x_serial;

create table tc1x_serial (
  id      integer        not null,
  dep     longblob       null
);

create unique index tc1x_serial_pk on tc1x_serial( id );

drop table if exists tc1x_rollback;

create table tc1x_rollback (
  id      numeric(10,0) not null,
  value1  varchar(200)  not null,
  value2  varchar(200),
  numb     numeric(10,0)
);

create unique index tc1x_rollback_pk on tc1x_rollback ( id );

drop table if exists tc1x_pks_person;

create table tc1x_pks_person (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  bday  datetime       null
);

create unique index tc1x_pks_person_pk on tc1x_pks_person( fname, lname );

drop table if exists tc1x_pks_employee;

create table tc1x_pks_employee (
  fname      varchar(15)    not null,
  lname      varchar(15)    not null,
  start_date datetime       null
);

create unique index tc1x_pks_employee_pk on tc1x_pks_employee( fname, lname );

drop table if exists tc1x_pks_payroll;

create table tc1x_pks_payroll (
  fname       varchar(15)  not null,
  lname       varchar(15)  not null,
  id          int          not null,
  holiday     int          not null,
  hourly_rate int          not null
);

create unique index tc1x_pks_payroll_fk on tc1x_pks_payroll( fname, lname );

create unique index tc1x_pks_payroll_pk on tc1x_pks_payroll( id );

drop table if exists tc1x_pks_address;

create table tc1x_pks_address (
  fname   varchar(15)  not null,
  lname   varchar(15)  not null,
  id      int          not null,
  street  varchar(30)  null,
  city    varchar(30)  null,
  state   varchar(2)   null,
  zip     varchar(6)   null
);

create unique index tc1x_pks_address_pk on tc1x_pks_address( id );

drop table if exists tc1x_pks_contract; 

create table tc1x_pks_contract (
  fname        varchar(15)  not null,
  lname        varchar(15)  not null,
  policy_no    int          not null,
  contract_no  int          not null,
  c_comment    varchar(90)  null
);

create unique index tc1x_pks_contract_fk on tc1x_pks_contract( fname, lname );

create unique index tc1x_pks_contract_pk on tc1x_pks_contract( policy_no, contract_no );

drop table if exists tc1x_pks_category_contract;

create table tc1x_pks_category_contract (
  policy_no    int      not null,
  contract_no  int      not null,
  cate_id      int      not null
);

drop table if exists tc1x_pks_category;

create table tc1x_pks_category (
  id    int             not null,
  name  varchar(20)     not null
);

create unique index tc1x_pks_category_pk on tc1x_pks_category( id );


-- tc2x TESTS

drop table if exists tc2x_master;

create table tc2x_master (
  id        numeric(10,0)  not null,
  value1    varchar(200)   not null,
  group_id  numeric(10,0)  null
);

create unique index tc2x_master_pk on tc2x_master ( id );

drop table if exists tc2x_detail;

create table tc2x_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1     varchar(200)   not null
);

create unique index tc2x_detail_pk on tc2x_detail ( detail_id );

drop table if exists tc2x_detail2;

create table tc2x_detail2 (
  detail2_id  numeric(10,0)  not null,
  detail_id   numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index tc2x_detail2_pk on tc2x_detail2 ( detail2_id );

drop table if exists tc2x_detail3;

create table tc2x_detail3 (
  detail3_id  numeric(10,0)  not null,
  detail_id   numeric(10,0)  not null,
  value1      varchar(200 )  not null
);

create unique index tc2x_detail3_pk on tc2x_detail3 ( detail3_id );

drop table if exists tc2x_group;

create table tc2x_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)   not null
);

create unique index tc2x_group_pk on tc2x_group ( id );

drop table if exists tc2x_depend2;
drop table if exists tc2x_depend_master;
drop table if exists tc2x_depend1;

create table tc2x_depend1 (
  id          int not null primary key
);

create table tc2x_depend_master (
  id          int not null primary key,
  depend1_id  int,
  index tc2x_master_depend1 ( depend1_id ),
  foreign key ( depend1_id ) references tc2x_depend1 ( id )
);

create table tc2x_depend2 (
  id          int not null primary key,
  master_id   int,
  index tc2x_depend2_master ( master_id ),
  foreign key ( master_id ) references tc2x_depend_master ( id )
);

drop table if exists tc2x_keygen;
create table tc2x_keygen (
  id    int           not null,
  attr  varchar(200)  not null
);

create unique index tc2x_keygen_pk on tc2x_keygen ( id );

drop table if exists tc2x_keygen_ext;
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

drop table if exists tc2x_uuid;

create table tc2x_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
);

create unique index tc2x_uuid_pk on tc2x_uuid ( id );

drop table if exists tc2x_uuid_ext;

create table tc2x_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
);

create unique index tc2x_uuid_ext_pk on tc2x_uuid_ext ( id );

drop table if exists tc2x_identity;

create table tc2x_identity (
  id    integer not null primary key auto_increment,
  attr  varchar(200) not null
);

drop table if exists tc2x_identity_ext;

create table tc2x_identity_ext (
  id   integer not null,
  ext  varchar(200) not null
);

create unique index tc2x_ident_ext_pk on tc2x_identity_ext ( id );

drop table if exists tc2x_seqtable;

create table tc2x_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index tc2x_seqtable_pk on tc2x_seqtable ( table_name );


-- tc3x TESTS

drop table if exists tc3x_entity;

create table tc3x_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc3x_entity_pk on tc3x_entity ( id );

drop table if exists tc3x_extends;

create table tc3x_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
);

create unique index tc3x_extends_pk on tc3x_extends ( id );


drop table if exists tc3x_call;

create table tc3x_call (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
);

create unique index tc3x_call_pk on tc3x_call ( id );


drop table if exists tc3x_group;

create table tc3x_group (
  id      numeric(10,0)  not null,
  value1  varchar(200)  not null
);

create unique index tc3x_group_pk on tc3x_group ( id );

drop table if exists tc3x_persistent;

create table tc3x_persistent (
  id        integer         not null,
  ctime     datetime        not null,
  mtime     datetime        null,
  value1    varchar(200)    not null,
  parent_id integer         null,
  group_id  numeric(10,0)   not null
);

create unique index tc3x_persistent_pk on tc3x_persistent ( id );


drop table if exists tc3x_related;

create table tc3x_related (
  id          integer     not null,
  persist_id  integer     not null
);

create unique index tc3x_related_pk on tc3x_related ( id );

drop table if exists tc3x_extends1;

create table tc3x_extends1 (
  ident   integer         not null,
  ext     integer         not null
);

create unique index tc3x_extends1_pk on tc3x_extends1 ( ident );


drop table if exists tc3x_extends2;

create table tc3x_extends2 (
  id      integer         not null,
  ext     integer         not null
);

create unique index tc3x_extends2_pk on tc3x_extends2 ( id );













-- UNDEFINED TESTS

drop table if exists test_table;

create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table_pk
   on test_table ( id );



drop table if exists test_group_person;

create table test_group_person (
  gid int         not null,
  pid int        not null 
);

create index test_group_person_p_pk on test_group_person ( pid );

create index test_group_person_g_pk on test_group_person ( gid );


drop table if exists test_many_group;

create table test_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
);

create unique index test_many_group_pk on test_many_group ( gid );


drop table if exists test_many_person;

create table test_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
);

create unique index test_many_person_pk on test_many_person ( pid );


drop table if exists tc8x_pks_person;

create table tc8x_pks_person (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  bday  datetime null
);

create unique index tc8x_pks_person_pk on tc8x_pks_person( fname, lname );


drop table if exists tc8x_pks_employee;

create table tc8x_pks_employee (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  start_date datetime null
);

create unique index tc8x_pks_person_employee_pk on tc8x_pks_employee( fname, lname );


drop table if exists tc8x_pks_payroll;

create table tc8x_pks_payroll (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create unique index tc8x_pks_payroll_fk on tc8x_pks_payroll( fname, lname );

create unique index tc8x_pks_payroll_pk on tc8x_pks_payroll( id );


drop table if exists tc8x_pks_project;

create table tc8x_pks_project (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id    int             not null,
  name  varchar(100)
);

create unique index tc8x_pks_project_pk on tc8x_pks_project( id );


drop table if exists tc8x_pks_address;

create table tc8x_pks_address (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  id int               not null,
  street varchar(30)    null,
  city  varchar(30)    null,
  state varchar(2)     null,
  zip varchar(6)       null
);

create unique index tc8x_pks_address_pk on tc8x_pks_address( id );


drop table if exists tc8x_pks_contract; 

create table tc8x_pks_contract (
  fname varchar(15)    not null,
  lname varchar(15)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(90)  null
);

create unique index tc8x_pks_contract_fk on tc8x_pks_contract( fname, lname );

create unique index tc8x_pks_contract_pk on tc8x_pks_contract( policy_no, contract_no );


drop table if exists tc8x_pks_category_contract;

create table tc8x_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
);


drop table if exists tc8x_pks_category;

create table tc8x_pks_category (
  id  int              not null,
  name varchar(20)     not null
);

create unique index tc8x_pks_category_pk on tc8x_pks_category( id );


drop table if exists list_types;

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

drop table if exists test_col;

create table test_col (
  id       integer         not null,
  dum    integer    null
);

create unique index test_col_pk on test_col( id );


drop table if exists test_item;

create table test_item (
  iid       integer         not null,
  id      integer         not null
);

create unique index test_item_pk on test_item( iid );

drop table if exists test_comp_item;

create table test_comp_item (
  iid       integer         not null,
  id      integer         not null
);

create unique index test_comp_item_pk on test_comp_item( iid );


drop table if exists test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );
create index test_oqltag_fk2 on test_oqltag( id2 );


drop table if exists tc8x_nton_a;
create table tc8x_nton_a (
  id         varchar(20)    not null,
  status     integer        not null
);

drop table if exists tc8x_nton_b;
create table tc8x_nton_b (
  id         varchar(20)    not null,
  status     integer        not null
);


-- those tables should be type INNODB with mysql 4
drop table if exists depend2;
drop table if exists master;
drop table if exists depend1;

create table depend1(
  id int not null primary key
);

create table master(
  depend1_id int ,
  id int not null primary key,
  index idx_master_depend1 (depend1_id),
  foreign key (depend1_id) references depend1(id)
);

create table depend2(
  master_id int,
  id int not null primary key,
  index idx_depend2_master (master_id),
  foreign key (master_id) references master(id)
);

drop table if exists tc8x_circ_brother;
drop table if exists tc8x_circ_sister;

create table tc8x_circ_brother (
	brother_id int not null,
	brother_sibling int,
	constraint pk_brother primary key (brother_id));

create table tc8x_circ_sister (
	sister_id int not null,
	sister_sibling int,
	constraint pk_sister primary key (sister_id));

-- tc166.TestLazy1to1
drop table if exists tc8x_lazy_11_chd;
create table tc8x_lazy_11_chd (
  id        int not null,
  descr     varchar(20) not null
);

drop table if exists tc8x_lazy_11_par;
create table tc8x_lazy_11_par (
  id        int not null,
  descr     varchar(20) not null,
  child_id  int
);

drop table if exists tc8x_lazy_11_author;
create table tc8x_lazy_11_author (
  id			bigint not null,
  first_name	varchar(100) not null,
  last_name		varchar(100) not null
);

drop table if exists tc8x_lazy_11_book;
create table tc8x_lazy_11_book (
  id			bigint not null,
  name 			varchar(100) not null,
  author_id		bigint not null
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

drop table if exists tc8x_enum_prod;
create table tc8x_enum_prod (
  id        int not null,
  name      varchar(200) not null,
  kind      varchar(200) not null
);

-- test objects for TestTransientAttribute 

drop table if exists tc8x_trans_master;
create table tc8x_trans_master (
  id        int not null,
  name      varchar(200) not null,
  propty1	int,
  propty2	int,
  propty3	int,
  ent2		int
);

drop table if exists tc8x_trans_child1;
create table tc8x_trans_child1 (
  id        int not null,
  descr     varchar(200) not null
);

drop table if exists tc8x_trans_child2;
create table tc8x_trans_child2 (
  id        int not null,
  entityOneId int not null,
  descr     varchar(200) not null
);

insert into tc8x_trans_master (id, name, propty1, propty2, ent2) values (1, 'entity1', 1, 2, 1);
insert into tc8x_trans_child1 (id, descr) values (1, 'description1');
insert into tc8x_trans_child2 (id, descr, entityOneId) values (1, 'description1', 1);
insert into tc8x_trans_child2 (id, descr, entityOneId) values (2, 'description2', 1);
insert into tc8x_trans_child2 (id, descr, entityOneId) values (3, 'description3', 1);


-- tc8x

drop table if exists tc8x_self_refer_parent;
create table tc8x_self_refer_parent (
  id        int not null primary key,
  fid		int,
  name      varchar(200) not null
);

insert into tc8x_self_refer_parent (id, fid, name) values (1, null, 'entity1');
insert into tc8x_self_refer_parent (id, fid, name) values (2, 1, 'entity2');
insert into tc8x_self_refer_parent (id, fid, name) values (3, 1, 'entity3');

drop table if exists tc8x_self_refer_child;
create table tc8x_self_refer_child (
  id        int not null primary key,
  descr     varchar(200) not null
);

insert into tc8x_self_refer_child (id, descr) values (1, 'description1');
insert into tc8x_self_refer_child (id, descr) values (2, 'description2');
insert into tc8x_self_refer_child (id, descr) values (3, 'description3');

DROP TABLE IF EXISTS tc8x_test_depends_ns;
CREATE TABLE IF NOT EXISTS tc8x_test_depends_ns (
  id int(11) NOT NULL auto_increment,
  master_id int(11) NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  PRIMARY KEY  (id),
  KEY master_id (master_id)
);

DROP TABLE IF EXISTS tc8x_test_master_ns;
CREATE TABLE IF NOT EXISTS tc8x_test_master_ns (
  id int(11) NOT NULL auto_increment,
  descrip varchar(50) NOT NULL default '',
  PRIMARY KEY  (id)
);

DROP TABLE IF EXISTS tc8x_test_depends_ns_nokg;
CREATE TABLE IF NOT EXISTS tc8x_test_depends_ns_nokg (
  id int(11) NOT NULL,
  master_id int(11) NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  PRIMARY KEY  (id)
);


DROP TABLE IF EXISTS tc8x_test_master_ns_nokg;
CREATE TABLE IF NOT EXISTS tc8x_test_master_ns_nokg (
  id int(11) NOT NULL,
  descrip varchar(50) NOT NULL default '',
  PRIMARY KEY  (id)
);

drop table if exists tc8x_parent;
create table tc8x_parent (
  id        int not null PRIMARY KEY,
  name      varchar(200) not null
);

insert into tc8x_parent (id, name) values (1, 'entity1');

drop table if exists tc8x_child;
create table tc8x_child (
  id        int not null PRIMARY KEY,
  descr     varchar(200) not null
);

insert into tc8x_child (id, descr) values (1, 'child1');

drop table if exists tc8x_parent_compound;
create table tc8x_parent_compound (
  id1       int not null,
  id2       int not null,
  name      varchar(200) not null,
  PRIMARY KEY (id1, id2)
);

insert into tc8x_parent_compound (id1, id2, name) values (1, 1, 'entityCompound1');

drop table if exists tc8x_child_compound;
create table tc8x_child_compound (
  id1       int not null,
  id2       int not null,
  descr     varchar(200) not null,
  PRIMARY KEY (id1, id2)
);

insert into tc8x_child_compound (id1, id2, descr) values (1, 1, 'childCompound1');


-- tc9x TESTS

drop table if exists poly_ordr;
create table poly_ordr (
  id int not null,
  name varchar (20) not null
);

drop table if exists poly_detail;
create table poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
);

drop table if exists poly_owner;
create table poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
);

drop table if exists poly_prod;
create table poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail	int not null,
  owner		int
);

drop table if exists poly_computer;
create table poly_computer (
  id   int not null,
  cpu  varchar(200) not null
);

drop table if exists poly_laptop;
create table poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
);

drop table if exists poly_server;
create table poly_server (
  id   int not null,
  numberOfCPUs  int not null,
  support int not null
);

drop table if exists poly_car;
create table poly_car (
  id   int not null,
  kw   int not null,
  make  varchar(200) not null
);

drop table if exists poly_truck;
create table poly_truck (
  id   int not null,
  max_weight   int not null
);

drop table if exists poly_prod_multi;
create table poly_prod_multi (
  id1        int not null,
  id2        int not null,
  name      varchar(200) not null,
  detail	int not null
);

drop table if exists poly_computer_multi;
create table poly_computer_multi (
  id1   int not null,
  id2        int not null,
  cpu  varchar(200) not null
);

drop table if exists poly_laptop_multi;
create table poly_laptop_multi (
  id1   int not null,
  id2        int not null,
  weight  int not null,
  resolution varchar(19) not null
);

drop table if exists poly_server_multi;
create table poly_server_multi (
  id1   int not null,
  id2        int not null,
  numberOfCPUs  int not null,
  support int not null
);

drop table if exists poly_order_product;
create table poly_order_product (
  order_id	int not null,
  product_id int not null
);

drop table if exists poly_table_m;
create table poly_table_m (
  id	int not null,
  name	varchar(20) not null
);

drop table if exists poly_table_n;
create table poly_table_n (
  id	int not null,
  name	varchar(20) not null
);

drop table if exists poly_m_n;
create table poly_m_n (
  m_id	int not null,
  n_id int not null
);

insert into poly_detail (id, category, location) values (1, 'category 1', 'location 1');
insert into poly_detail (id, category, location) values (2, 'category 2', 'location 2');
insert into poly_detail (id, category, location) values (3, 'category 3', 'location 3');
insert into poly_detail (id, category, location) values (4, 'category 4', 'location 4');
insert into poly_detail (id, category, location) values (5, 'category 5', 'location 5');

insert into poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1);
insert into poly_computer (id, cpu) values (1, 'centrino');
insert into poly_laptop (id, weight, resolution) values (1, 2800, '1280');

insert into poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2);
insert into poly_computer (id, cpu) values (2, 'centrino');
insert into poly_laptop (id, weight, resolution) values (2, 2700, '1024');

insert into poly_prod (id, name, detail, owner) values (3, 'server 3', 3, 3);
insert into poly_computer (id, cpu) values (3, 'pentium 4');
insert into poly_server (id, numberOfCPUs, support) values (3, 4, 3);

insert into poly_prod (id, name, detail, owner) values (4, 'server 4', 4, 4);
insert into poly_computer (id, cpu) values (4, 'pentium 4');
insert into poly_server (id, numberOfCPUs, support) values (4, 16,5);

insert into poly_prod (id, name, detail, owner) values (5, 'truck 5', 5, 5);
insert into poly_car (id, kw, make) values (5, 60, 'make 5');
insert into poly_truck (id, max_weight) values (5, 4);

insert into poly_prod_multi (id1, id2, name, detail) values (1, 1, 'laptop 1', 1);
insert into poly_computer_multi (id1, id2, cpu) values (1, 1, 'centrino');
insert into poly_laptop_multi (id1, id2, weight, resolution) values (1, 1, 2800, '1280');

insert into poly_prod_multi (id1, id2, name, detail) values (2, 2, 'laptop 2', 2);
insert into poly_computer_multi (id1, id2, cpu) values (2, 2, 'centrino');
insert into poly_laptop_multi (id1, id2, weight, resolution) values (2, 2, 2700, '1024');

insert into poly_prod_multi (id1, id2, name, detail) values (3, 3, 'server 3', 3);
insert into poly_computer_multi (id1, id2, cpu) values (3, 3, 'pentium 4');
insert into poly_server_multi (id1,  id2, numberOfCPUs, support) values (3, 3, 4, 3);

insert into poly_prod_multi (id1, id2, name, detail) values (4, 4, 'server 4', 4);
insert into poly_computer_multi (id1, id2, cpu) values (4, 4, 'pentium 4');
insert into poly_server_multi (id1, id2, numberOfCPUs, support) values (4, 4, 16,5);

insert into poly_owner (id, name, product) values (1, 'owner 1', 1);
insert into poly_owner (id, name, product) values (2, 'owner 2', 2);
insert into poly_owner (id, name, product) values (3, 'owner 3', 3);
insert into poly_owner (id, name, product) values (4, 'owner 4', 4);
insert into poly_owner (id, name, product) values (5, 'owner 5', 5);

insert into poly_ordr (id, name) values (1, 'order 1');

insert into poly_order_product (order_id, product_id) values (1, 1);
insert into poly_order_product (order_id, product_id) values (1, 2);

insert into poly_m_n (m_id, n_id) values (1, 1);
insert into poly_m_n (m_id, n_id) values (1, 2);

insert into poly_table_m (id, name) values (1, "m1");
insert into poly_table_m (id, name) values (2, "m2");

insert into poly_table_n (id, name) values (1, "n1");
insert into poly_table_n (id, name) values (2, "n2");

/* 
* Two objects, a Derived and a Container. 
* Since Derived derives from Base there is a row in Base too.
* The Container id = 200
* The Derived id = 100
*/
DROP TABLE IF EXISTS poly_base;
CREATE TABLE poly_base (
  id varchar(64) NOT NULL default '',
  color varchar(64) default NULL,
  PRIMARY KEY  (ID)
) ;

INSERT INTO poly_base VALUES ('100','red');

DROP TABLE IF EXISTS poly_derived;
CREATE TABLE poly_derived (
  id varchar(64) NOT NULL default '',
  scent varchar(64) default NULL,
  PRIMARY KEY  (ID)
) ;
INSERT INTO poly_derived VALUES ('100','vanilla');


DROP TABLE IF EXISTS poly_container;
CREATE TABLE poly_container (
  id varchar(64) NOT NULL default '',
  reference varchar(64) default NULL,
  PRIMARY KEY  (ID)
) ;
INSERT INTO poly_container VALUES ('200','100');

DROP TABLE IF EXISTS poly_Product;
CREATE TABLE poly_Product(
  IdProd NUMERIC(10) PRIMARY KEY,
  NameProd   VARCHAR(30) NULL,
  DescProd   VARCHAR(30) NULL);

DROP TABLE IF EXISTS poly_ActProduct;
CREATE TABLE poly_ActProduct(
  IdAct NUMERIC(10) PRIMARY KEY REFERENCES Product (IdProd),
  BestSeason VARCHAR(30) NULL);

DROP TABLE IF EXISTS poly_ComposedOffer;
CREATE TABLE poly_ComposedOffer(
  IdCOffer NUMERIC(10) PRIMARY KEY REFERENCES Product (IdProd),
  NameCO   VARCHAR(30) NULL,
  DescCO   VARCHAR(30) NULL);

DROP TABLE IF EXISTS poly_OfferComposition;
CREATE TABLE poly_OfferComposition(
  Offer NUMERIC(10),
  Product NUMERIC(10), 
  CONSTRAINT unique_rel UNIQUE (Offer, Product) );

-- tables required for TestPolymorphismDependedndObject

DROP TABLE IF EXISTS poly_extend_object;
CREATE TABLE poly_extend_object (
  id            int NOT NULL default '0',
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO poly_extend_object VALUES (1, 'This is the extended object.');

DROP TABLE IF EXISTS poly_base_object;
CREATE TABLE poly_base_object (
  id           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
);

INSERT INTO poly_base_object VALUES (1, 'This is the test object.', '0');

DROP TABLE IF EXISTS poly_depend_object;
CREATE TABLE poly_depend_object (
  id           int NOT NULL default '0',
  parentId           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO poly_depend_object VALUES(1, 1, 'This is a description');

# TC129 

DROP TABLE IF EXISTS container;
CREATE TABLE container (
  id int(11) unsigned NOT NULL auto_increment,
  name varchar(200) NULL,
  prop int(11) unsigned default NULL,
  PRIMARY KEY  (id)
);

INSERT INTO container (id, name, prop) VALUES 
  (1,'Container 1',1),
  (2,'Container 2',2),
  (3,'Container 3',3),
  (4,'Container 4',4);

DROP TABLE IF EXISTS container_item;
CREATE TABLE container_item (
  id int(11) unsigned NOT NULL auto_increment,
  item int(11) unsigned default NULL,
  value varchar(200) NULL,
  PRIMARY KEY  (id)
);

INSERT INTO container_item (id, item, value) VALUES 
  (1,1,'Container item 1'),
  (2,2,'Container item 2'),
  (3,3,'Container item 3'),
  (4,4,'Container item 4'),
  (5,1,'Container item 5'),
  (6,2,'Container item 6'),
  (7,3,'Container item 7'),
  (8,4,'Container item 8');


# TC128a

drop table if exists sorted_container;
create table sorted_container (
  id        int not null,
  name      varchar(200) not null
);

drop table if exists sorted_item;
create table sorted_item(
  id        int not null,
  id_1		int not null,
  name      varchar(200) not null
);

insert into sorted_container(id, name) values (1, 'container 1');
insert into sorted_container(id, name) values (2, 'container 2');
insert into sorted_container(id, name) values (1, 'container 3');

insert into sorted_item (id, id_1, name) values (1, 1, 'container item 1');
insert into sorted_item (id, id_1, name) values (2, 1, 'container item 2');
insert into sorted_item (id, id_1, name) values (3, 2, 'container item 3');
 
