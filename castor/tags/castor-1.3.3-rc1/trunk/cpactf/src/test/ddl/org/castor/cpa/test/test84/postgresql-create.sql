create table test84_trans_master (
  id        int not null,
  name      varchar(200) not null,
  propty1    int,
  propty2    int,
  propty3    int,
  ent2        int
);

create table test84_trans_child1 (
  id        int not null,
  descr     varchar(200) not null
);

create table test84_trans_child2 (
  id        int not null,
  descr     varchar(200) not null
);

insert into test84_trans_master (id, name, propty1, propty2, ent2) values (1, 'entity1', 1, 2, 1);
insert into test84_trans_child1 (id, descr) values (1, 'description1');
insert into test84_trans_child2 (id, descr) values (1, 'description1');
insert into test84_trans_child2 (id, descr) values (2, 'description2');
insert into test84_trans_child2 (id, descr) values (3, 'description3');
