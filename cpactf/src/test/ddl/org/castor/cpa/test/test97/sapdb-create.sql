create table test97_poly_ordr (
  id int not null,
  name varchar (20) not null
)
//

create table test97_poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
)
//

create table test97_poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
)
//

create table test97_poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail    int not null,
  owner        int
)
//

create table test97_poly_computer (
  id   int not null,
  cpu  varchar(200) not null
)
//

create table test97_poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
)
//


create table test97_poly_server (
  id   int not null,
  numberOfCPUs  int not null,
  support int not null
)
//

create table test97_poly_car (
  id   int not null,
  kw   int not null,
  make  varchar(200) not null
)
//

create table test97_poly_truck (
  id   int not null,
  max_weight   int not null
)
//

create table test97_poly_prod_multi (
  id1        int not null,
  id2        int not null,
  name      varchar(200) not null
)
//


create table test97_poly_computer_multi (
  id1   int not null,
  id2        int not null,
  cpu  varchar(200) not null
)
//

create table test97_poly_laptop_multi (
  id1   int not null,
  id2        int not null,
  weight  int not null,
  resolution varchar(19) not null
)
//

create table test97_poly_server_multi (
  id1   int not null,
  id2        int not null,
  numberOfCPUs  int not null,
  support int not null
)
//

create table test97_poly_order_product (
  order_id    int not null,
  product_id int not null
)
//

create table test97_poly_table_m (
  id    int not null,
  name    varchar(20) not null
)
//

create table test97_poly_table_n (
  id    int not null,
  name    varchar(20) not null
)
//


create table test97_poly_m_n (
  m_id    int not null,
  n_id int not null
)
//

insert into test97_poly_detail (id, category, location) values (1, 'category 1', 'location 1')
//
insert into test97_poly_detail (id, category, location) values (2, 'category 2', 'location 2')
//
insert into test97_poly_detail (id, category, location) values (3, 'category 3', 'location 3')
//
insert into test97_poly_detail (id, category, location) values (4, 'category 4', 'location 4')
//
insert into test97_poly_detail (id, category, location) values (5, 'category 5', 'location 5')
//

insert into test97_poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1)
//
insert into test97_poly_computer (id, cpu) values (1, 'centrino')
//
insert into test97_poly_laptop (id, weight, resolution) values (1, 2800, '1280')
//

insert into test97_poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2)
//
insert into test97_poly_computer (id, cpu) values (2, 'centrino')
//
insert into test97_poly_laptop (id, weight, resolution) values (2, 2700, '1024')
//

insert into test97_poly_prod (id, name, detail, owner) values (3, 'server 3', 3, 3)
//
insert into test97_poly_computer (id, cpu) values (3, 'pentium 4')
//
insert into test97_poly_server (id, numberOfCPUs, support) values (3, 4, 3)
//

insert into test97_poly_prod (id, name, detail, owner) values (4, 'server 4', 4, 4)
//
insert into test97_poly_computer (id, cpu) values (4, 'pentium 4')
//
insert into test97_poly_server (id, numberOfCPUs, support) values (4, 16,5)
//

insert into test97_poly_prod (id, name, detail, owner) values (5, 'truck 5', 5, 5)
//
insert into test97_poly_car (id, kw, make) values (5, 60, 'make 5')
//
insert into test97_poly_truck (id, max_weight) values (5, 4)
//

insert into test97_poly_prod_multi (id1, id2, name) values (1, 1, 'laptop 1')
//
insert into test97_poly_computer_multi (id1, id2, cpu) values (1, 1, 'centrino')
//
insert into test97_poly_laptop_multi (id1, id2, weight, resolution) values (1, 1, 2800, '1280')
//

insert into test97_poly_prod_multi (id1, id2, name) values (2, 2, 'laptop 2')
//
insert into test97_poly_computer_multi (id1, id2, cpu) values (2, 2, 'centrino')
//
insert into test97_poly_laptop_multi (id1, id2, weight, resolution) values (2, 2, 2700, '1024')
//

insert into test97_poly_prod_multi (id1, id2, name) values (3, 3, 'server 3')
//
insert into test97_poly_computer_multi (id1, id2, cpu) values (3, 3, 'pentium 4')
//
insert into test97_poly_server_multi (id1,  id2, numberOfCPUs, support) values (3, 3, 4, 3)
//

insert into test97_poly_prod_multi (id1, id2, name) values (4, 4, 'server 4')
//
insert into test97_poly_computer_multi (id1, id2, cpu) values (4, 4, 'pentium 4')
//
insert into test97_poly_server_multi (id1, id2, numberOfCPUs, support) values (4, 4, 16,5)
//

insert into test97_poly_owner (id, name, product) values (1, 'owner 1', 1)
//
insert into test97_poly_owner (id, name, product) values (2, 'owner 2', 2)
//
insert into test97_poly_owner (id, name, product) values (3, 'owner 3', 3)
//
insert into test97_poly_owner (id, name, product) values (4, 'owner 4', 4)
//
insert into test97_poly_owner (id, name, product) values (5, 'owner 5', 5)
//

insert into test97_poly_ordr (id, name) values (1, 'order 1')
//

insert into test97_poly_order_product (order_id, product_id) values (1, 1)
//
insert into test97_poly_order_product (order_id, product_id) values (1, 2)
//

insert into test97_poly_m_n (m_id, n_id) values (1, 1)
//
insert into test97_poly_m_n (m_id, n_id) values (1, 2)
//

insert into test97_poly_table_m (id, name) values (1, 'm1')
//
insert into test97_poly_table_m (id, name) values (2, 'm2')
//

insert into test97_poly_table_n (id, name) values (1, 'n1')
//
insert into test97_poly_table_n (id, name) values (2, 'n2')
//
