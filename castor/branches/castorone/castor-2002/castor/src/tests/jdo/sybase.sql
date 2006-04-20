-- test_table
drop table test_table
go
create table test_table (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index test_table_pk on test_table ( id )
go
grant all on test_table to test
go

-- test many to many
drop table test_group_person
go
create table test_group_person (
  gid int         not null,
  pid int        not null 
)
go
create index test_group_person_p_pk on test_group_person ( pid )
go
create index test_group_person_g_pk on test_group_person ( gid )
go
grant all on test_group_person to test
go

drop table test_many_group
go
create table test_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
)
go
create unique index test_many_group_pk on test_many_group ( gid )
go
grant all on test_many_group to test
go

drop table test_many_person
go
create table test_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
)
go

create unique index test_many_person_pk on test_many_person ( pid )
go
grant all on test_many_person to test
go

-- test multiple pk
drop table test_pks_person
go
create table test_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  datetime
)
go

create unique index test_pks_person_pk on test_pks_person( fname, lname )
go
grant all on test_pks_person to test
go

drop table test_pks_employee
go
create table test_pks_employee (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  start_date datetime null
)
go
create unique index test_pks_person_employee_pk on test_pks_employee( fname, lname )
go
grant all on test_pks_employee to test
go

drop table test_pks_payroll
go
create table test_pks_payroll (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
)
go
create unique index test_pks_payroll_fk on test_pks_payroll( fname, lname )
go
create unique index test_pks_payroll_pk on test_pks_payroll( id )
go

drop table test_pks_address
go
create table test_pks_address (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  street varchar(30) null,
  city  varchar(30) null,
  state varchar(2) null,
  zip varchar(6) null
)
go
create unique index test_pks_address_pk on test_pks_address( id )
go
grant all on test_pks_address to test
go

drop table test_pks_contract
go
create table test_pks_contract (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(100)  null
)
go
create unique index test_pks_contract_fk on test_pks_contract( fname, lname )
go
create unique index test_pks_contract_pk on test_pks_contract( policy_no, contract_no )
go
grant all on test_pks_contract to test
go

drop table test_pks_category_contract
go
create table test_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
)
go

drop table test_pks_category
go
create table test_pks_category (
  id  int              not null,
  name varchar(100)     not null
)
go
create unique index test_pks_category_pk on test_pks_category( id )
go

-- base class
drop table test_rel_person
go
create table test_rel_person (
  pid    int             not null,
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  datetime 
)
go

create unique index test_rel_person_pk on test_rel_person( pid )
go
grant all on test_rel_person to test
go

-- extend base class (person)
drop table test_rel_employee
go
create table test_rel_employee (
  pid    int             not null,
  start_date datetime null
)
go
create unique index test_rel_person_employee_pk on test_rel_employee( pid )
go
grant all on test_rel_employee to test
go

-- depends class of person
drop table test_rel_address
go
create table test_rel_address (
  pid    int             not null,
  id  int               not null,
  street varchar(30) null,
  city  varchar(30) null,
  state varchar(2) null,
  zip varchar(6) null
)
go
create index test_rel_address_fk on test_rel_address( pid )
go
create unique index test_rel_address_pk on test_rel_address( id )
go
grant all on test_rel_address to test
go


-- depend class of employee
drop table test_rel_payroll
go
create table test_rel_payroll (
  pid    int             not null,
  id int               not null,
  holiday int,
  hourly_rate int
)
go
create unique index test_rel_payroll_fk on test_rel_payroll( pid )
go
create unique index test_rel_payroll_pk on test_rel_payroll( id )
go
-- end for test_relations


-- test_table_ex
drop table test_table_ex
go
create table test_table_ex (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index test_table_ex_pk on test_table_ex ( id )
go
grant all on test_table_ex to test
go

-- test_table_extends
drop table test_table_extends
go
create table test_table_extends (
  id      int          not null,
  value3  varchar(200) null,
  value4  varchar(200) null
)
go
create unique index test_table_extends_pk on test_table_extends ( id )
go
grant all on test_table_extends to test
go


-- test_race
drop table test_race
go
create table test_race
(
  id      int          not null,
  value1  int          not null
)
go
create unique index test_race_pk on test_race ( id )
go
grant all on test_race to test
go


-- test_master
drop table test_master
go
create table test_master (
  id       numeric(10,0)    not null,
  value1    varchar(200)   not null,
  group_id numeric(10,0)  null
)
go
create unique index test_master_pk on test_master ( id )
go
grant all on test_master to test
go


-- test_detail
drop table test_detail
go
create table test_detail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
)
go
create unique index test_detail_pk on test_detail ( detail_id )
go
grant all on test_detail to test
go


-- test_group
drop table test_group
go
create table test_group (
  id     numeric(10,0)  not null,
  value1  varchar(200)   not null
)
go
create unique index test_group_pk on test_group ( id )
go
grant all on test_group to test
go


-- test_types
drop table test_types
go
create table test_types (
  id       numeric(10,0)  not null,
  tdt      datetime       not null,
  ttm      smalldatetime  not null,
  int_val  integer        null,
  long_val numeric(18,0)  null,
  char_val char(1)        null,
  bool_val char(1)        null,
  int_date integer        null,
  str_time char(12)       null,
  num_date numeric(17,0)  null
)
go
create unique index test_types_pk on test_types (id)
go
grant all on test_types to test
go


-- test_keygen
drop table test_keygen
go
create table test_keygen (
  id   int          not null,
  attr varchar(200) not null
)
go
create unique index test_keygen_pk on test_keygen ( id )
go
grant all on test_keygen to test
go


-- test_keygen_ext
drop table test_keygen_ext
go
create table test_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
)
go
create unique index test_keygen_ext_pk on test_keygen_ext ( id )
go
grant all on test_keygen_ext to test
go

-- list_types
drop table list_types
go
create table list_types (
  o_char  CHAR         null,
  o_nchar NCHAR        null,
  o_varchar VARCHAR(10) null,
  o_nvarchar VARCHAR(10) null,
  o_text TEXT null,
  o_datetime datetime null,
  o_smalldatetime SMALLDATETIME null,
  o_binary BINARY(10) null,
  o_varbinary VARBINARY(10) null,
  o_image IMAGE null,
  o_int   INT null,
)
go


-- test_seqtable
drop table test_seqtable
go
create table test_seqtable (
  table_name varchar(200) not null,
  max_id     int          null
)
go
create unique index test_seqtable_pk on test_seqtable ( table_name )
go
grant all on test_seqtable to test
go

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


-- The test stored procedure on TransactSQL
drop procedure proc_check_permissions
go
create procedure proc_check_permissions @userName varchar(200),
                                        @groupName varchar(200) AS
    SELECT id, value1, value2 FROM test_table WHERE value1 = @userName
    SELECT id, value1, value2 FROM test_table WHERE value2 = @groupName
go
sp_procxmode proc_check_permissions, "anymode"
go


-- test_detail2
drop table test_detail2
go
create table test_detail2
(
  detail2_id  numeric(10,0)  not null,
  detail_id  numeric(10,0)  not null,
  value1      varchar(200 )  not null
)
go
create unique index test_detail2_pk on test_detail2 ( detail2_id )
go
grant all on test_detail2 to test
go



-- test_persistent
drop table test_persistent
go
create table test_persistent (
  id       integer         not null,
  ctime    datetime        not null,
  mtime    datetime        null,
  value1    varchar(200)    not null,
  parent_id integer        null,
  group_id numeric(10,0)   not null
)
go
create unique index test_persistent_pk on test_persistent ( id )
go
grant all on test_persistent to test
go
