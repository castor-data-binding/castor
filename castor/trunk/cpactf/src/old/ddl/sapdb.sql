-- tc0x TESTS

drop table tc0x_sample
//

create table tc0x_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
//

create unique index tc0x_sample_pk on tc0x_sample ( id )
//

drop table tc0x_race
//

create table tc0x_race (
  id      int          not null,
  value1  int          not null
)
//

create unique index tc0x_race_pk on tc0x_race ( id )
//

-- UNDEFINED TESTS

drop table list_types
//

create table list_types (
  o_char  CHAR  null,
  o_nchar CHAR  null,
  o_varchar VARCHAR(20) null,
  o_varchar2 VARCHAR(20) null,
  o_clob LONG null,
  o_long LONG null,
  o_number FIXED null,
  o_int   INT null,
  o_date timestamp null,
  o_raw   CHAR (20) BYTE   null,
  o_blob  LONG BYTE     null,
  o_bfile LONG BYTE     null
)
//

drop table test_oqltag
//

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
)
//

create index test_oqltag_fk1 on test_oqltag( id1 )
//

create index test_oqltag_fk2 on test_oqltag( id2 )
//
