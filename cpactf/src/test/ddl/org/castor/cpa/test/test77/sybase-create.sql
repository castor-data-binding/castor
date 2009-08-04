create table test77_depend1(
  id int not null,
  constraint test77_depend1_pk primary key (id)
)
go

create table test77_master(
  id int not null,
  depend1_id int,
  constraint test77_master_pk primary key (id)
)
go

create table test77_depend2(
  id int not null,
  master_id int,
  constraint test77_depend2_pk primary key (id)
)
go

alter table test77_master
    add constraint test77_master_depend1_fk
    foreign key (depend1_id) references test77_depend1 (id)
go

alter table test77_depend2
    add constraint test77_depend2_master_fk
    foreign key (master_id) references test77_master(id)
go
