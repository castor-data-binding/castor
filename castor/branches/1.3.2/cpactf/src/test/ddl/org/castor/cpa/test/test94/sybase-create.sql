create table test94_poly_car (
  id   int not null,
  kw   int not null,
  make  varchar(200) not null
)
go

create table test94_poly_computer (
  id   int not null,
  cpu  varchar(200) not null
)
go

create table test94_poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
)
go

create table test94_poly_ordr (
  id int not null,
  name varchar (20) not null
)
go

create table test94_poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
)
go

create table test94_poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail    int not null,
  owner        int
)
go

create table test94_poly_order_product (
  order_id    int not null,
  product_id int not null
)
go

create table test94_poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
)
go

create table test94_poly_truck (
  id   int not null,
  max_weight   int not null
)
go

insert into test94_poly_detail (id, category, location) values (1, 'category 1', 'location 1')
go
insert into test94_poly_detail (id, category, location) values (2, 'category 2', 'location 2')
go
insert into test94_poly_detail (id, category, location) values (3, 'category 3', 'location 3')
go
insert into test94_poly_detail (id, category, location) values (4, 'category 4', 'location 4')
go
insert into test94_poly_detail (id, category, location) values (5, 'category 5', 'location 5')
go

insert into test94_poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1)
go
insert into test94_poly_computer (id, cpu) values (1, 'centrino')
go
insert into test94_poly_laptop (id, weight, resolution) values (1, 2800, '1280')
go

insert into test94_poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2)
go
insert into test94_poly_computer (id, cpu) values (2, 'centrino')
go
insert into test94_poly_laptop (id, weight, resolution) values (2, 2700, '1024')
go

insert into test94_poly_prod (id, name, detail, owner) values (5, 'truck 5', 5, 5)
go
insert into test94_poly_car (id, kw, make) values (5, 60, 'make 5')
go
insert into test94_poly_truck (id, max_weight) values (5, 4)
go

insert into test94_poly_owner (id, name, product) values (1, 'owner 1', 1)
go
insert into test94_poly_owner (id, name, product) values (2, 'owner 2', 2)
go
insert into test94_poly_owner (id, name, product) values (3, 'owner 3', 3)
go
insert into test94_poly_owner (id, name, product) values (4, 'owner 4', 4)
go
insert into test94_poly_owner (id, name, product) values (5, 'owner 5', 5)
go

insert into test94_poly_ordr (id, name) values (1, 'order 1')
go

insert into test94_poly_order_product (order_id, product_id) values (1, 1)
go
insert into test94_poly_order_product (order_id, product_id) values (1, 2)
go
