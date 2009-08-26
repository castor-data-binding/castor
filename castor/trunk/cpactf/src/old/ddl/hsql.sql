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

-- UNDEFINED TESTS

drop table if exists test_table2;

create table test_table2 (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table2_pk
   on test_table2 ( id );

/*
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
*/

drop table if exists test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );
create index test_oqltag_fk2 on test_oqltag( id2 );

