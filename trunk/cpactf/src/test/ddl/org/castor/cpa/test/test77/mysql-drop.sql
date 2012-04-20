alter table test77_master drop constraint test77_master_depend1_fk;
alter table test77_depend2 drop constraint test77_depend2_master_fk;

drop table if exists test77_depend2;
drop table if exists test77_master;
drop table if exists test77_depend1;