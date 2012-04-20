create table test29_depend1 (
  id          int,
  primary key ( id )
)
//

create table test29_depend_master (
  id          int,
  depend1_id  int,
  primary key ( id ),
  foreign key test29_master_depend1 ( depend1_id )
  references test29_depend1 ( id ) on delete cascade
)
//

create table test29_depend2 (
  id         int,
  master_id  int,
  primary key ( id ),
  foreign key test29_depend2_master ( master_id )
  references test29_depend_master ( id ) on delete cascade
)
//