drop table if exists entity;

create table entity (
  id        int not null,
  name      varchar(200) not null
);

insert into entity (id, name) values (1, 'entity1');
