create table test29_depend1 (
  id          int not null,
  constraint test29_depend1_pk primary key ( id )
)
/
create table test29_depend_master (
  id          int not null,
  depend1_id  int,
  constraint test29_depend_master_pk primary key ( id )
)
/
alter table test29_depend_master
  add constraint test29_master_depend1
  foreign key ( depend1_id ) references test29_depend1 ( id )
/
create table test29_depend2(
  id          int not null,
  master_id   int,
  constraint test29_depend2_pk primary key ( id )
)
/
alter table test29_depend2
  add constraint test29_depend2_master
  foreign key ( master_id ) references test29_depend_master ( id )
  /