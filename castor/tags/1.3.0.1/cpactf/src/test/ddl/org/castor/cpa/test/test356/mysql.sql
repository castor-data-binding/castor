drop table if exists test356_entity;

create table test356_entity (
  id       int not null,
  prop     int,
  primary key (id)
);

insert into test356_entity (id, prop) values (1, 100);
insert into test356_entity (id, prop) values (2, 200);
insert into test356_entity (id, prop) values (3, 300);
insert into test356_entity (id, prop) values (4, 400);
insert into test356_entity (id, prop) values (5, null);
