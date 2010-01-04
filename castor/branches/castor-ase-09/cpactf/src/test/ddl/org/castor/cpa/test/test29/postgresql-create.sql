create table test29_depend1 (
  id          int not null primary key
);

create table test29_depend_master (
  depend1_id  int ,
  id          int not null primary key
);

alter table test29_depend_master
  add constraint test29_master_depend1
  foreign key ( depend1_id ) references test29_depend1 ( id );

create table test29_depend2 (
  master_id   int,
  id          int not null primary key
);

alter table test29_depend2
  add constraint test29_depend2_master
  foreign key ( master_id ) references test29_depend_master ( id );