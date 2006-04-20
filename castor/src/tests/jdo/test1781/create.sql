drop table entity1;
create table entity1 (
  id        int not null,
  name      varchar(200) not null
);

-- create unique index entity1_pk on entity1 ( id );
-- grant all privileges on entity1 to test;

insert into entity1 (id, name) values (1, 'entity1');
