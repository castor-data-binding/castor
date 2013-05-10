create table test303_entity2(
  id integer,
  name varchar(255) not null,
  primary key (id),
  UNIQUE (name)
);

create table test303_entity1(
  id integer,
  related integer,
  primary key (id)
);