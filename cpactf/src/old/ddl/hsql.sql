-- tc0x TESTS

drop table if exists tc0x_sample;

create table tc0x_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index tc0x_sample_pk on tc0x_sample ( id );

drop table if exists tc0x_race;

create table tc0x_race (
  id      int          not null,
  value1  int          not null
);

create unique index tc0x_race_pk on tc0x_race ( id );

-- UNDEFINED TESTS

drop table if exists test_table2;

create table test_table2 (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test_table2_pk
   on test_table2 ( id );

/*
drop table if exists list_types;

create table list_types (
  o_char  CHAR(100)         null,
  o_nchar NATIONAL CHAR(100)   null,
  o_varchar VARCHAR(20) null,
  o_varchar2 VARCHAR(20) null,
  o_clob LONGTEXT null,
  o_long LONGBLOB null,
  o_number NUMERIC null,
  o_int   INT null,
  o_date datetime null,
  o_raw   TINYBLOB     null,
  o_blob  BLOB         null,
  o_bfile MEDIUMBLOB  null
);
*/

drop table if exists test_oqltag;

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);

create index test_oqltag_fk1 on test_oqltag( id1 );
create index test_oqltag_fk2 on test_oqltag( id2 );

-- tc9x TESTS

drop table if exists tc9x_foo;
create table tc9x_foo (
  id                int not null,
  field             varchar(200) not null
);

insert into tc9x_foo (id, field) values (1, 'a foo');
insert into tc9x_foo (id, field) values (2, 'a bar');

drop table if exists tc9x_bar;
create table tc9x_bar (
  id        int not null
);

insert into tc9x_bar (id) values (2);

drop table if exists tc9x_customer;
create table tc9x_customer (
    id                int not null,
    description     varchar(200) not null            
);

insert into tc9x_customer (id, description) values (1, 'alice');
insert into tc9x_customer (id, description) values (2, 'bob');

drop table if exists tc9x_subscription;
create table tc9x_subscription (
    id                int not null,
    createddate        datetime,
    description        varchar(200),
    customer_id        int not null
);

insert into tc9x_subscription (id, createddate, customer_id) values (1, now(), 2);
insert into tc9x_subscription (id, createddate, customer_id) values (2, now(), 2);

drop table if exists tc9x_supersubscription;
create table tc9x_supersubscription (
    id            int not null
);

drop table if exists tc9x_credit;
create table tc9x_credit (
    id                        int not null,
    balance                    int not null,
    supersubscription_id    int not null
);

drop table if exists tc9x_poly_ordr;
create table tc9x_poly_ordr (
  id int not null,
  name varchar (20) not null
);

drop table if exists tc9x_poly_detail;
create table tc9x_poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
);

drop table if exists tc9x_poly_owner;
create table tc9x_poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
);

drop table if exists tc9x_poly_prod;
create table tc9x_poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail    int not null,
  owner        int
);

drop table if exists tc9x_poly_computer;
create table tc9x_poly_computer (
  id   int not null,
  cpu  varchar(200) not null
);

drop table if exists tc9x_poly_laptop;
create table tc9x_poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
);

drop table if exists tc9x_poly_server;
create table tc9x_poly_server (
  id   int not null,
  numberOfCPUs  int not null,
  support int not null
);

drop table if exists tc9x_poly_car;
create table tc9x_poly_car (
  id   int not null,
  kw   int not null,
  make  varchar(200) not null
);

drop table if exists tc9x_poly_truck;
create table tc9x_poly_truck (
  id   int not null,
  max_weight   int not null
);

drop table if exists tc9x_poly_prod_multi;
create table tc9x_poly_prod_multi (
  id1        int not null,
  id2        int not null,
  name      varchar(200) not null
);

drop table if exists tc9x_poly_computer_multi;
create table tc9x_poly_computer_multi (
  id1   int not null,
  id2        int not null,
  cpu  varchar(200) not null
);

drop table if exists tc9x_poly_laptop_multi;
create table tc9x_poly_laptop_multi (
  id1   int not null,
  id2        int not null,
  weight  int not null,
  resolution varchar(19) not null
);

drop table if exists tc9x_poly_server_multi;
create table tc9x_poly_server_multi (
  id1   int not null,
  id2        int not null,
  numberOfCPUs  int not null,
  support int not null
);

drop table if exists tc9x_poly_order_product;
create table tc9x_poly_order_product (
  order_id    int not null,
  product_id int not null
);

drop table if exists tc9x_poly_table_m;
create table tc9x_poly_table_m (
  id    int not null,
  name    varchar(20) not null
);

drop table if exists tc9x_poly_table_n;
create table tc9x_poly_table_n (
  id    int not null,
  name    varchar(20) not null
);

drop table if exists tc9x_poly_m_n;
create table tc9x_poly_m_n (
  m_id    int not null,
  n_id int not null
);

insert into tc9x_poly_detail (id, category, location) values (1, 'category 1', 'location 1');
insert into tc9x_poly_detail (id, category, location) values (2, 'category 2', 'location 2');
insert into tc9x_poly_detail (id, category, location) values (3, 'category 3', 'location 3');
insert into tc9x_poly_detail (id, category, location) values (4, 'category 4', 'location 4');
insert into tc9x_poly_detail (id, category, location) values (5, 'category 5', 'location 5');

insert into tc9x_poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1);
insert into tc9x_poly_computer (id, cpu) values (1, 'centrino');
insert into tc9x_poly_laptop (id, weight, resolution) values (1, 2800, '1280');

insert into tc9x_poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2);
insert into tc9x_poly_computer (id, cpu) values (2, 'centrino');
insert into tc9x_poly_laptop (id, weight, resolution) values (2, 2700, '1024');

insert into tc9x_poly_prod (id, name, detail, owner) values (3, 'server 3', 3, 3);
insert into tc9x_poly_computer (id, cpu) values (3, 'pentium 4');
insert into tc9x_poly_server (id, numberOfCPUs, support) values (3, 4, 3);

insert into tc9x_poly_prod (id, name, detail, owner) values (4, 'server 4', 4, 4);
insert into tc9x_poly_computer (id, cpu) values (4, 'pentium 4');
insert into tc9x_poly_server (id, numberOfCPUs, support) values (4, 16,5);

insert into tc9x_poly_prod (id, name, detail, owner) values (5, 'truck 5', 5, 5);
insert into tc9x_poly_car (id, kw, make) values (5, 60, 'make 5');
insert into tc9x_poly_truck (id, max_weight) values (5, 4);

insert into tc9x_poly_prod_multi (id1, id2, name) values (1, 1, 'laptop 1');
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (1, 1, 'centrino');
insert into tc9x_poly_laptop_multi (id1, id2, weight, resolution) values (1, 1, 2800, '1280');

insert into tc9x_poly_prod_multi (id1, id2, name) values (2, 2, 'laptop 2');
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (2, 2, 'centrino');
insert into tc9x_poly_laptop_multi (id1, id2, weight, resolution) values (2, 2, 2700, '1024');

insert into tc9x_poly_prod_multi (id1, id2, name) values (3, 3, 'server 3');
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (3, 3, 'pentium 4');
insert into tc9x_poly_server_multi (id1,  id2, numberOfCPUs, support) values (3, 3, 4, 3);

insert into tc9x_poly_prod_multi (id1, id2, name) values (4, 4, 'server 4');
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (4, 4, 'pentium 4');
insert into tc9x_poly_server_multi (id1, id2, numberOfCPUs, support) values (4, 4, 16,5);

insert into tc9x_poly_owner (id, name, product) values (1, 'owner 1', 1);
insert into tc9x_poly_owner (id, name, product) values (2, 'owner 2', 2);
insert into tc9x_poly_owner (id, name, product) values (3, 'owner 3', 3);
insert into tc9x_poly_owner (id, name, product) values (4, 'owner 4', 4);
insert into tc9x_poly_owner (id, name, product) values (5, 'owner 5', 5);

insert into tc9x_poly_ordr (id, name) values (1, 'order 1');

insert into tc9x_poly_order_product (order_id, product_id) values (1, 1);
insert into tc9x_poly_order_product (order_id, product_id) values (1, 2);

insert into tc9x_poly_m_n (m_id, n_id) values (1, 1);
insert into tc9x_poly_m_n (m_id, n_id) values (1, 2);

insert into tc9x_poly_table_m (id, name) values (1, 'm1');
insert into tc9x_poly_table_m (id, name) values (2, 'm2');

insert into tc9x_poly_table_n (id, name) values (1, 'n1');
insert into tc9x_poly_table_n (id, name) values (2, 'n2');

drop table if exists tc9x_poly_base;
create table tc9x_poly_base (
  id varchar(64) not null,
  color varchar(64) default null,
  primary key  (ID)
) ;

insert into tc9x_poly_base values ('100','red');

drop table if exists tc9x_poly_derived;
create table tc9x_poly_derived (
  id varchar(64) not null,
  scent varchar(64) default null,
  primary key  (ID)
) ;
insert into tc9x_poly_derived values ('100','vanilla');


drop table if exists tc9x_poly_container;
create table tc9x_poly_container (
  id varchar(64) not null,
  reference varchar(64) default null,
  primary key  (ID)
) ;
insert into tc9x_poly_container values ('200','100');

drop table if exists tc9x_poly_Product;
create table tc9x_poly_Product(
  IdProd numeric(10) primary key,
  NameProd   varchar(30) null,
  DescProd   varchar(30) null
);

drop table if exists tc9x_poly_ActProduct;
create table tc9x_poly_ActProduct(
  IdAct int primary key,
  BestSeason varchar(30) null
);

drop table if exists tc9x_poly_ComposedOffer;
create table tc9x_poly_ComposedOffer(
  IdCOffer numeric(10) primary key,
  NameCO   varchar(30) null,
  DescCO   varchar(30) null
);

drop table if exists tc9x_poly_OfferComposition;
create table tc9x_poly_OfferComposition(
  Offer numeric(10),
  Product numeric(10), 
  constraint unique_rel unique (Offer, Product)
);

-- tables required for TestPolymorphismDependedndObject

DROP TABLE tc9x_poly_extend_object;
CREATE TABLE tc9x_poly_extend_object (
  id            int NOT NULL default '0',
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO tc9x_poly_extend_object VALUES (1, 'This is the extended object.');

DROP TABLE tc9x_poly_base_object;
CREATE TABLE tc9x_poly_base_object (
  id           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
);

INSERT INTO tc9x_poly_base_object VALUES (1, 'This is the test object.', '0');

DROP TABLE tc9x_poly_depend_object;
CREATE TABLE tc9x_poly_depend_object (
  id           int NOT NULL default '0',
  parentId           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO tc9x_poly_depend_object VALUES(1, 1, 'This is a description');

# TC20x - self-referential relations 
 
drop table tc200_self_relation_folder;
create table tc200_self_relation_folder (
  id          int                 not null,  
  name        varchar(255)        not null,
  parent_id      int                null
  
);

drop table tc200_self_relation_parent;
create table tc200_self_relation_parent (
  id          int                 not null,  
  name        varchar(255)        not null
);

drop table tc200_self_relation_extend;
create table tc200_self_relation_extend (
  id          int                 not null,  
  parent_id      int                null
);
