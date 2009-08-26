spool oracle.log

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

-- base class
drop table test_rel_person;

create table test_rel_person (
  pid    int             not null,
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  date
);

create unique index test_rel_person_pk on test_rel_person( pid );

-- grant all on test_rel_person to test;


-- extend base class (person)
drop table test_rel_employee;

create table test_rel_employee (
  pid    int             not null,
  start_date date null
);

create unique index test_rel_employee_pk on test_rel_employee( pid );

-- grant all on test_rel_employee to test;


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

-- grant all on test_rel_address to test;


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

-- grant all on test_rel_payroll to test;
-- end for test_relations

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

-- grant all on list_types to test;


drop table test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );

create index test_oqltag_fk2 on test_oqltag( id2 );

-- grant all on test_oqltag to test;

