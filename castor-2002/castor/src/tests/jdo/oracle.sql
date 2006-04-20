-- test_table
drop table   test_table;
create table test_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);
create unique index test_table_pk
   on test_table ( id );
grant all on test_table to test;

-- test many to many
drop table test_group_person;
create table test_group_person (
  gid int         not null,
  pid int        not null 
);
create index test_group_person_p_pk on test_group_person ( pid );
create index test_group_person_g_pk on test_group_person ( gid );
grant all on test_group_person to test;

drop table test_many_group;
create table test_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
);
create unique index test_many_group_pk on test_many_group ( gid );
grant all on test_many_group to test;

drop table test_many_person;
create table test_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
);
create unique index test_many_person_pk on test_many_person ( pid );
grant all on test_many_person to test;


-- test multiple pk
drop table test_pks_person;
create table test_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date null
);
create unique index test_pks_person_pk on test_pks_person( fname, lname );
grant all on test_pks_person to test;

drop table test_pks_employee;
create table test_pks_employee (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  start_date date null
);
create unique index test_pks_person_employee_pk on test_pks_employee( fname, lname );
grant all on test_pks_employee to test;

drop table test_pks_payroll;
create table test_pks_payroll (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
);
create unique index test_pks_payroll_fk on test_pks_payroll( fname, lname );
create unique index test_pks_payroll_pk on test_pks_payroll( id );

drop table test_pks_address;
create table test_pks_address (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  street varchar(30)    null,
  city  varchar(30)    null,
  state varchar(2)     null,
  zip varchar(6)       null
);
create unique index test_pks_address_pk on test_pks_address( id );
grant all on test_pks_address to test;

drop table test_pks_contract;
create table test_pks_contract (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(100)  null
);
create unique index test_pks_contract_fk on test_pks_contract( fname, lname );
create unique index test_pks_contract_pk on test_pks_contract( policy_no, contract_no );
grant all on test_pks_contract to test;

drop table test_pks_category_contract;
create table test_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
);

drop table test_pks_category;
create table test_pks_category (
  id  int              not null,
  name varchar(100)     not null
);
create unique index test_pks_category_pk on test_pks_category( id );

-- base class
drop table test_rel_person;

create table test_rel_person (
  pid    int             not null,
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date 
);


create unique index test_rel_person_pk on test_rel_person( pid );


-- extend base class (person)
drop table test_rel_employee;

create table test_rel_employee (
  pid    int             not null,
  start_date date null
);

create unique index test_rel_employee_pk on test_rel_employee( pid );


-- depends class of person
drop table test_rel_address;

create table test_rel_address (
  pid    int             not null,
  id  int               not null,
  street varchar(30) null,
  city  varchar(30) null,
  state varchar(30) null,
  zip varchar(30) null
);

create index test_rel_address_fk on test_rel_address( pid );

create unique index test_rel_address_pk on test_rel_address( id );


-- depend class of employee
drop table test_rel_payroll;

create table test_rel_payroll (
  pid    int             not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create index test_rel_payroll_fk on test_rel_payroll( pid );

create unique index test_rel_payroll_pk on test_rel_payroll( id );

-- end for test_relations

-- test_table_extends
drop table test_table_extends;

create table test_table_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
);

create unique index test_table_extends_pk on test_table_extends ( id );

grant all on test_table_extends to test;


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
  value1    varchar(200)   not null,
  group_id numeric(10,0)  null
);
create unique index test_master_pk
  on test_master ( id );
grant all on test_master to test;


-- test_detail
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
  tdt      date           not null,
  ttm      date           not null,
  int_val  integer        null,
  long_val numeric(18,0)  null,
  char_val char(1)        null,
  bool_val char(1)        null,
  int_date integer        null,
  str_time char(12)       null,
  num_date numeric(17,0)  null,
  blob_val blob           null,
  clob_val clob           null,
  blob_val2 blob          null,
  clob_val2 clob          null,
  date_str  date          null
);
create unique index test_types_pk
  on test_types ( id );
grant all on test_types to test;


-- test_keygen
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
grant all on test_seqtable to test;


drop sequence   test_keygen_seq;
create sequence test_keygen_seq;
grant all on test_keygen_seq to test;


-- test the identity key generator
drop table test_identity
go
create table test_identity (
  id numeric(10,0) identity,
  attr varchar(200) not null,
)
go
grant all on test_identity to test
go

drop table test_identity_ext
go
create table test_identity_ext (
  id numeric(10,0) not null,
  ext varchar(200) not null,
)
go
create unique index test_ident_ext_pk on test_identity_ext ( id )
go
grant all on test_identity_ext to test
go

-- test_col
drop table test_col;
create table test_col (
  id       integer         not null,
  dum    integer    null
);
create unique index test_col_pk on test_col( id );

drop table test_item;
create table test_item (
  iid       integer         not null,
  id      integer           null
);
create unique index test_item_pk on test_item( iid );


-- test_serial
drop table test_serial;
create table test_serial (
  id      integer        not null,
  dep     blob           null
);
create unique index test_serial_pk on test_serial( id );


-- test_persistent
drop table test_persistent;
create table test_persistent (
  id       integer         not null,
  ctime    date            not null,
  mtime    date            null,
  value1   varchar(200)    not null,
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

-- list_types
drop table list_types;
create table list_types (
  o_char  CHAR         null,
  o_nchar NCHAR        null,
  o_varchar VARCHAR(20) null,
  o_varchar2 VARCHAR2(20) null,
  o_clob CLOB null,
  o_long LONG null,
  o_number NUMBER null,
  o_int   INT null,
  o_date DATE null,
  o_raw   RAW(20)      null,
--  o_longraw LONG RAW    null,
  o_blob  BLOB         null,
  o_bfile BFILE        null
);


drop table test_oqlext;
create table test_oqlext (
  ident   integer         not null,
  ext     integer         not null
);
create unique index test_oqlext_pk on test_oqlext( ident );
