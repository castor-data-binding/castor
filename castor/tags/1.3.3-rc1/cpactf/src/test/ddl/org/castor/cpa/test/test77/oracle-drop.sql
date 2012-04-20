alter table test77_master drop constraint test77_master_depend1_fk
/
alter table test77_depend2 drop constraint test77_depend2_master_fk
/

drop table test77_master
/
drop table test77_depend1
/
drop table test77_depend2
/
