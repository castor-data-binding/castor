create table test303_entity2(
  id integer,
  name varchar(255) unique not null,
  primary key (id)
)
go

create table test303_entity1(
  id integer,
  related integer,
  primary key (id)
)
go
