create table test29_depend1 (
  id          int not null primary key
)
go

create table test29_depend_master (
  id          int not null primary key,
  depend1_id  int,
  index test29_master_depend1 ( depend1_id ),
  foreign key ( depend1_id ) references test29_depend1 ( id )
)
go

create table test29_depend2 (
  id          int not null primary key,
  master_id   int,
  index test29_depend2_master ( master_id ),
  foreign key ( master_id ) references test29_depend_master ( id )
)
go