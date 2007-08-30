drop table if exists entity;

create table entity (
  id       int not null,
  prop     int,
  primary key (id)
);

insert into entity (id, prop) values (1, 100);
insert into entity (id, prop) values (2, 200);
insert into entity (id, prop) values (3, 300);
insert into entity (id, prop) values (4, 400);
insert into entity (id, prop) values (5, null);
