create table test72_sorted_container (
  id        int not null,
  name      varchar(200) not null
)
go

create table test72_sorted_item(
  id        int not null,
  id_1		int not null,
  name      varchar(200) not null
)
go

insert into test72_sorted_container(id, name) values (1, 'container 1')
go
insert into test72_sorted_container(id, name) values (2, 'container 2')
go
insert into test72_sorted_container(id, name) values (1, 'container 3')
go

insert into test72_sorted_item (id, id_1, name) values (1, 1, 'container item 1')
go
insert into test72_sorted_item (id, id_1, name) values (2, 1, 'container item 2')
go
insert into test72_sorted_item (id, id_1, name) values (3, 2, 'container item 3')
go
