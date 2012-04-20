create table test95_poly_computer (
  id   int not null,
  cpu  varchar(200) not null
)
//

create table test95_poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
)
//

create table test95_poly_ordr (
  id int not null,
  name varchar (20) not null
)
//

create table test95_poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
)
//

create table test95_poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail    int not null,
  owner        int
)
//

create table test95_poly_order_product (
  order_id    int not null,
  product_id int not null
)
//

create table test95_poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
)
//

insert into test95_poly_detail (id, category, location) values (1, 'category 1', 'location 1')
//
insert into test95_poly_detail (id, category, location) values (2, 'category 2', 'location 2')
//
insert into test95_poly_detail (id, category, location) values (3, 'category 3', 'location 3')
//
insert into test95_poly_detail (id, category, location) values (4, 'category 4', 'location 4')
//
insert into test95_poly_detail (id, category, location) values (5, 'category 5', 'location 5')
//

insert into test95_poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1)
//
insert into test95_poly_computer (id, cpu) values (1, 'centrino')
//
insert into test95_poly_laptop (id, weight, resolution) values (1, 2800, '1280')
//

insert into test95_poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2)
//
insert into test95_poly_computer (id, cpu) values (2, 'centrino')
//
insert into test95_poly_laptop (id, weight, resolution) values (2, 2700, '1024')
//

insert into test95_poly_owner (id, name, product) values (1, 'owner 1', 1)
//
insert into test95_poly_owner (id, name, product) values (2, 'owner 2', 2)
//
insert into test95_poly_owner (id, name, product) values (3, 'owner 3', 3)
//
insert into test95_poly_owner (id, name, product) values (4, 'owner 4', 4)
//
insert into test95_poly_owner (id, name, product) values (5, 'owner 5', 5)
//

insert into test95_poly_ordr (id, name) values (1, 'order 1')
//

insert into test95_poly_order_product (order_id, product_id) values (1, 1)
//
insert into test95_poly_order_product (order_id, product_id) values (1, 2)
//
