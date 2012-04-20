alter table test76_master drop constraint test76_master_depend1_fk
go
alter table test76_depend2 drop constraint test76_depend2_master_fk
go

drop table test76_master
go
drop table test76_depend1
go
drop table test76_depend2
go
