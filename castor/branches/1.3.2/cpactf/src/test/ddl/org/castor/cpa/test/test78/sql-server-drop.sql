drop table test78_table
go

alter table test78_master drop constraint test78_master_depend1_fk
go
alter table test78_depend2 drop constraint test78_depend2_master_fk
go

drop table test78_master
go
drop table test78_depend1
go
drop table test78_depend2
go
