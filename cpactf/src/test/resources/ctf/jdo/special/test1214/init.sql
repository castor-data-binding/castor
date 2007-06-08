drop table ent;
create table ent (
  id       int not null,
  prop     int,
  primary key (id)
);

insert into ent (id, prop) values (1, 100);
insert into ent (id, prop) values (2, 200);
insert into ent (id, prop) values (3, 300);
insert into ent (id, prop) values (4, 400);
insert into ent (id, prop) values (5, null);
