drop table if exists test78_table;

alter table test78_master drop constraint test78_master_depend1_fk;
alter table test78_depend2 drop constraint test78_depend2_master_fk;

drop table if exists test78_depend2;
drop table if exists test78_master;
drop table if exists test78_depend1;