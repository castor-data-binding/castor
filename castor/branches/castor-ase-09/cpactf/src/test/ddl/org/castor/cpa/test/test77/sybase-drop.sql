alter table test77_master drop constraint test77_master_depend1_fk
go
alter table test77_depend2 drop constraint test77_depend2_master_fk
go

drop table test77_master
go
drop table test77_depend1
go
drop table test77_depend2
go
