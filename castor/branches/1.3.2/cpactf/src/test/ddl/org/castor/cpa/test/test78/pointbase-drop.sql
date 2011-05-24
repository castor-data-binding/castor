drop table test78_table;

alter table test78_master drop constraint test78_master_depend1_fk;
alter table test78_depend2 drop constraint test78_depend2_master_fk;

drop table test78_master;
drop table test78_depend1;
drop table test78_depend2;
