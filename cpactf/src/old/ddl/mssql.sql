-- tc0x TESTS

drop table tc0x_sample
go
create table tc0x_sample (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index tc0x_sample_pk on tc0x_sample ( id )
go
grant all on tc0x_sample to test
go

drop table tc0x_race
go
create table tc0x_race (
  id      int          not null,
  value1  int          not null
)
go
create unique index tc0x_race_pk on tc0x_race ( id )
go
grant all on tc0x_race to test
go

-- UNDEFINED TESTS

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
grant all on test_rel_payroll to test
go
-- end for test_relations

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
  o_int   INT null
)
go
grant all on list_types to test
go

drop table if exists test_oqltag;
go

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);
go

create index test_oqltag_fk1 on test_oqltag( id1 );
go

create index test_oqltag_fk2 on test_oqltag( id2 );
go

grant all on test_oqltag to test
go

