drop table if exists test2177_entity;

create table test2177_entity (
  id        int not null,
  name      varchar(200) not null
);

insert into test2177_entity (id, name) values (1, 'entity1');
