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


-- test_smaster
drop table test_smaster
go
create table test_smaster (
  id       numeric(10,0)    not null
)
go
create unique index test_smaster_pk on test_smaster ( id )
go


-- test_sdetail
drop table test_sdetail
go
create table test_sdetail (
  detail_id  numeric(10,0)  not null,
  master_id  numeric(10,0)  not null
)
go
create unique index test_sdetail_pk on test_sdetail ( detail_id )
go
create index test_sdetail_fk on test_sdetail ( master_id )
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
  dbl_val  numeric(14,2)  not null,
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
