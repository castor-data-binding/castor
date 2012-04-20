create table tc7x_as_assoc1 (
  id        int not null,
  name      varchar(200) not null
);

insert into tc7x_as_assoc1 (id, name) values (1, 'assoc1');

create table tc7x_as_main (
  id        int not null,
  name      varchar(200) not null,
  assoc1_id    int default null
);

insert into tc7x_as_main (id, name, assoc1_id) values (1, 'main', 1);

create table tc7x_as_main_many (
  id        int not null,
  name      varchar(200) not null
);

insert into tc7x_as_main_many (id, name) values (1, 'main');

create table tc7x_as_assoc_many (
  id        int not null,
  name      varchar(200) not null,
  main_id    int
);

insert into tc7x_as_assoc_many (id, name, main_id) values (1, 'assoc.many.1', 1);
insert into tc7x_as_assoc_many (id, name, main_id) values (2, 'assoc.many.2', 1);
