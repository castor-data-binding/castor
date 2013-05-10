create table test78_table (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
//
create unique index test78_table_uq on test78_table ( id )
//
   
create table test78_depend1(
  id int not null,
  constraint test78_depend1_pk primary key (id)
)
//

create table test78_master(
  id int not null,
  depend1_id int,
  constraint test78_master_pk primary key (id)
)
//

create table test78_depend2(
  id int not null,
  master_id int,
  constraint test78_depend2_pk primary key (id)
)
//

alter table test78_master
    add constraint test78_master_depend1_fk
    foreign key (depend1_id) references test78_depend1 (id)
//

alter table test78_depend2
    add constraint test78_depend2_master_fk
    foreign key (master_id) references test78_master(id)
//
