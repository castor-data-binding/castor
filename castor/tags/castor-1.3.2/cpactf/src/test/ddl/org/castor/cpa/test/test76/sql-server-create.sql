create table test76_depend1(
  id int not null,
  constraint test76_depend1_pk primary key (id)
)
go

create table test76_master(
  id int not null,
  depend1_id int,
  constraint test76_master_pk primary key (id)
)
go

create table test76_depend2(
  id int not null,
  master_id int,
  constraint test76_depend2_pk primary key (id)
)
go

alter table test76_master
    add constraint test76_master_depend1_fk
    foreign key (depend1_id) references test76_depend1 (id)
go

alter table test76_depend2
    add constraint test76_depend2_master_fk
    foreign key (master_id) references test76_master(id)
go
