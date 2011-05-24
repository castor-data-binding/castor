alter table test76_master drop constraint test76_master_depend1_fk;
alter table test76_depend2 drop constraint test76_depend2_master_fk;

drop table if exists test76_depend2;
drop table if exists test76_master;
drop table if exists test76_depend1;