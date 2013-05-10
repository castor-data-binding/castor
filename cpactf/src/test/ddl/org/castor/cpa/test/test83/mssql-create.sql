create table test83_parent (
  id        int not null PRIMARY KEY,
  name      varchar(200) not null
)
go

insert into test83_parent (id, name) values (1, 'entity1')
go

create table test83_child (
  id        int not null PRIMARY KEY,
  descr     varchar(200) not null
)
go

insert into test83_child (id, descr) values (1, 'child1')
go

create table test83_parent_compound (
  id1       int not null,
  id2       int not null,
  name      varchar(200) not null,
  PRIMARY KEY (id1, id2)
)
go

insert into test83_parent_compound (id1, id2, name) values (1, 1, 'entityCompound1')
go

create table test83_child_compound (
  id1       int not null,
  id2       int not null,
  descr     varchar(200) not null,
  PRIMARY KEY (id1, id2)
)
go

insert into test83_child_compound (id1, id2, descr) values (1, 1, 'childCompound1')
go
