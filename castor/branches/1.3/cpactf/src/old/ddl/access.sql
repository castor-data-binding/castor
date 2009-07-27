----Driver info----
--sun.jdbc.odbc.JdbcOdbcDriver
--jdbc:odbc:test
--test
--test
----Initalize SQL Statment----
drop table test_table;
create table test_table ( id int not null, value1 varchar(200) not null, value2 varchar(200) );
create unique index test_table_pk on test_table ( id );
drop table test_table_ex
create table test_table_ex ( id int not null, value1 varchar(200) not null, value2 varchar(200) null )
create unique index test_table_ex_pk on test_table_ex ( id )
drop table test_race
create table test_race ( id int not null, value1 int not null )
create unique index test_race_pk on test_race ( id )
drop table test_master;
create table test_master ( id int not null, "value1" varchar(200) not null, group_id int not null );
create unique index test_master_pk on test_master ( id );
drop table test_detail;
create table test_detail ( detail_id int not null,  master_id int not null, "value1" varchar(200) not null );
create unique index test_detail_pk  on test_detail ( detail_id );
drop table test_detail2;
create table test_detail2 ( detail2_id  numeric(10,0) not null, detail_id numeric(10,0) not null, value1 varchar(200 ) not null);
create unique index test_detail2_pk on test_detail2 ( detail2_id );
drop table test_group;
create table test_group ( id int not null, "value1" varchar(200)  not null );
create unique index test_group_pk on test_group ( id );
drop table test_types;
create table test_types ( id int not null, tdt date not null, ttm date not null, int_val integer null, long_val numeric(18,0) null, char_val char(1) null, bool_val char(1) null, bool_is_method char(1) null );
create unique index test_types_pk on test_types ( id );
drop table test_keygen;
create table test_keygen ( id int not null, attr  varchar(200)  not null );
create unique index test_keygen_pk on test_keygen ( id );
drop table test_keygen_ext;
create table test_keygen_ext ( id int not null, ext varchar(200) not null );
create unique index test_keygen_ext_pk on test_keygen_ext ( id );
drop table test_seqtable;
create table test_seqtable ( table_name varchar(200) not null, max_id int );
create unique index test_seqtable_pk on test_seqtable ( table_name );
drop table test_persistent;
create table test_persistent ( id integer not null, ctime date not null, mtime date null, value1 varchar(200) not null, parent_id integer null);
create unique index test_persistent_pk on test_persistent ( id );

