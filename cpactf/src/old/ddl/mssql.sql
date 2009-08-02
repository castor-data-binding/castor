-- tc0x TESTS

drop table tc0x_sample
go
create table tc0x_sample (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index tc0x_sample_pk on tc0x_sample ( id )
go
grant all on tc0x_sample to test
go

drop table tc0x_race
go
create table tc0x_race (
  id      int          not null,
  value1  int          not null
)
go
create unique index tc0x_race_pk on tc0x_race ( id )
go
grant all on tc0x_race to test
go

-- UNDEFINED TESTS

-- tc7x_table
drop table tc7x_table
go
create table tc7x_table (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index tc7x_table_pk on tc7x_table ( id )
go
grant all on tc7x_table to test
go

-- test many to many
drop table 
go
drop table tc7x_many_group
go
drop table tc7x_many_group
go

create table tc7x_many_group (
  gid       int           not null,
  value1    varchar(100)  not null
)
go
create unique index tc7x_many_group_pk on tc7x_many_group ( gid )
go
grant all on tc7x_many_group to test
go

create table tc7x_many_group (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null
)
go
create unique index tc7x_many_group_pk on tc7x_many_group ( pid )
go
grant all on tc7x_many_group to test
go

create table  (
  gid int         not null,
  pid int        not null,
  CONSTRAINT person_delete
    FOREIGN KEY(pid) 
    REFERENCES tc7x_many_group(pid),
  CONSTRAINT group_delete
    FOREIGN KEY(gid) 
    REFERENCES tc7x_many_group(gid)
)
go
create index _p_pk on  ( pid )
go
create index _g_pk on  ( gid )
go
grant all on  to test
go

-- test multiple pk
drop table tc8x_pks_person
go
create table tc8x_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  datetime
)
go
create unique index tc8x_pks_person_pk on tc8x_pks_person( fname, lname )
go
grant all on tc8x_pks_person to test
go

drop table tc8x_pks_employee
go
create table tc8x_pks_employee (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  start_date datetime null
)
go
create unique index tc8x_pks_person_employee_pk on tc8x_pks_employee( fname, lname )
go
grant all on tc8x_pks_employee to test
go

drop table tc8x_pks_payroll
go
create table tc8x_pks_payroll (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
)
go
create unique index tc8x_pks_payroll_fk on tc8x_pks_payroll( fname, lname )
go
create unique index tc8x_pks_payroll_pk on tc8x_pks_payroll( id )
go
grant all on tc8x_pks_payroll to test
go

drop table tc8x_pks_project
go
create table tc8x_pks_project (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id    int             not null,
  name  varchar(100)
)
go
create unique index tc8x_pks_project_pk on tc8x_pks_project( id )
go
grant all on tc8x_pks_payroll to test
go

drop table tc8x_pks_address
go
create table tc8x_pks_address (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  street varchar(30) null,
  city  varchar(30) null,
  state varchar(2) null,
  zip varchar(6) null
)
go
create unique index tc8x_pks_address_pk on tc8x_pks_address( id )
go
grant all on tc8x_pks_address to test
go

drop table tc8x_pks_contract
go
create table tc8x_pks_contract (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(100)  null
)
go
create unique index tc8x_pks_contract_fk on tc8x_pks_contract( fname, lname )
go
create unique index tc8x_pks_contract_pk on tc8x_pks_contract( policy_no, contract_no )
go
grant all on tc8x_pks_contract to test
go

drop table tc8x_pks_category_contract
go
create table tc8x_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
)
go
grant all on tc8x_pks_category_contract to test
go

drop table tc8x_pks_category
go
create table tc8x_pks_category (
  id  int              not null,
  name varchar(100)     not null
)
go
create unique index tc8x_pks_category_pk on tc8x_pks_category( id )
go
grant all on tc8x_pks_category to test
go

-- base class
drop table test_rel_person
go
create table test_rel_person (
  pid    int             not null,
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  datetime
)
go
create unique index test_rel_person_pk on test_rel_person( pid )
go
grant all on test_rel_person to test
go

-- extend base class (person)
drop table test_rel_employee
go
create table test_rel_employee (
  pid    int             not null,
  start_date datetime null
)
go
create unique index test_rel_person_employee_pk on test_rel_employee( pid )
go
grant all on test_rel_employee to test
go

-- depends class of person
drop table test_rel_address
go
create table test_rel_address (
  pid    int             not null,
  id  int               not null,
  street varchar(30) null,
  city  varchar(30) null,
  state varchar(2) null,
  zip varchar(6) null
)
go
create index test_rel_address_fk on test_rel_address( pid )
go
create unique index test_rel_address_pk on test_rel_address( id )
go
grant all on test_rel_address to test
go

-- depend class of employee
drop table test_rel_payroll
go
create table test_rel_payroll (
  pid    int             not null,
  id int               not null,
  holiday int,
  hourly_rate int
)
go
create unique index test_rel_payroll_fk on test_rel_payroll( pid )
go
create unique index test_rel_payroll_pk on test_rel_payroll( id )
go
grant all on test_rel_payroll to test
go
-- end for test_relations

-- list_types
drop table list_types
go
create table list_types (
  o_char  CHAR         null,
  o_nchar NCHAR        null,
  o_varchar VARCHAR(10) null,
  o_nvarchar VARCHAR(10) null,
  o_text TEXT null,
  o_datetime datetime null,
  o_smalldatetime SMALLDATETIME null,
  o_binary BINARY(10) null,
  o_varbinary VARBINARY(10) null,
  o_image IMAGE null,
  o_int   INT null
)
go
grant all on list_types to test
go

drop table if exists test_oqltag;
go

create table test_oqltag (
  id1   integer         not null,
  id2   integer         not null
);
go

create index test_oqltag_fk1 on test_oqltag( id1 );
go

create index test_oqltag_fk2 on test_oqltag( id2 );
go

grant all on test_oqltag to test
go


drop table tc8x_nton_a
go

create table tc8x_nton_a (
  id         varchar(20)      not null,
  status     int              not null
)
go

grant all on tc8x_nton_a to test
go

drop table tc8x_nton_b
go

create table tc8x_nton_b (
  id         varchar(20)      not null,
  status     int              not null
)
go

grant all on tc8x_nton_b to test
go

drop table tc8x_enum_prod
go

create table tc8x_enum_prod (
  id        int not null,
  name      varchar(200) not null,
  kind      varchar(200) not null
)
go

grant all on tc8x_enum_prod to test
go

-- test objects for TestTransientAttribute 

drop table tc8x_trans_master
go

create table tc8x_trans_master (
  id        int not null,
  name      varchar(200) not null,
  propty1    int,
  propty2    int,
  propty3    int,
  ent2        int
)
go

drop table tc8x_trans_child1
go

create table tc8x_trans_child1 (
  id        int not null,
  descr     varchar(200) not null
)
go

drop table tc8x_trans_child2
go

create table tc8x_trans_child2 (
  id        int not null,
  descr     varchar(200) not null
)
go

insert into tc8x_trans_master (id, name, propty1, propty2, ent2) values (1, 'entity1', 1, 2, 1)
go

insert into tc8x_trans_child1 (id, descr) values (1, 'description1')
go

insert into tc8x_trans_child2 (id, descr) values (1, 'description1')
go

insert into tc8x_trans_child2 (id, descr) values (2, 'description2')
go

insert into tc8x_trans_child2 (id, descr) values (3, 'description3')
go

-- tc9x TESTS

drop table tc9x_poly_ordr
go
create table tc9x_poly_ordr (
  id int not null,
  name varchar (20) not null
)
go

drop table tc9x_poly_detail
go
create table tc9x_poly_detail (
  id int not null,
  category varchar (20) not null,
  location varchar (20) not null
)
go

drop table tc9x_poly_owner
go
create table tc9x_poly_owner (
  id int not null,
  name varchar (20) not null,
  product int not null
)
go

drop table tc9x_poly_prod
go
create table tc9x_poly_prod (
  id        int not null,
  name      varchar(200) not null,
  detail    int not null,
  owner        int
)
go

drop table tc9x_poly_computer
go
create table tc9x_poly_computer (
  id   int not null,
  cpu  varchar(200) not null
)
go

drop table tc9x_poly_laptop
go
create table tc9x_poly_laptop (
  id   int not null,
  weight  int not null,
  resolution varchar(19) not null
)
go

drop table tc9x_poly_server
go
create table tc9x_poly_server (
  id   int not null,
  numberOfCPUs  int not null,
  support int not null
)
go

drop table tc9x_poly_car
go
create table tc9x_poly_car (
  id   int not null,
  kw   int not null,
  make  varchar(200) not null
)
go

drop table tc9x_poly_truck
go
create table tc9x_poly_truck (
  id   int not null,
  max_weight   int not null
)
go

drop table tc9x_poly_prod_multi
go
create table tc9x_poly_prod_multi (
  id1        int not null,
  id2        int not null,
  name      varchar(200) not null
)
go

drop table tc9x_poly_computer_multi
go
create table tc9x_poly_computer_multi (
  id1   int not null,
  id2        int not null,
  cpu  varchar(200) not null
)
go

drop table tc9x_poly_laptop_multi
go
create table tc9x_poly_laptop_multi (
  id1   int not null,
  id2        int not null,
  weight  int not null,
  resolution varchar(19) not null
)
go

drop table tc9x_poly_server_multi
go
create table tc9x_poly_server_multi (
  id1   int not null,
  id2        int not null,
  numberOfCPUs  int not null,
  support int not null
)
go

drop table tc9x_poly_order_product
go
create table tc9x_poly_order_product (
  order_id    int not null,
  product_id int not null
)
go

drop table tc9x_poly_table_m
go
create table tc9x_poly_table_m (
  id    int not null,
  name    varchar(20) not null
)
go

drop table tc9x_poly_table_n
go
create table tc9x_poly_table_n (
  id    int not null,
  name    varchar(20) not null
)
go

drop table tc9x_poly_m_n
go
create table tc9x_poly_m_n (
  m_id    int not null,
  n_id int not null
)
go


insert into tc9x_poly_detail (id, category, location) values (1, 'category 1', 'location 1')
go
insert into tc9x_poly_detail (id, category, location) values (2, 'category 2', 'location 2')
go
insert into tc9x_poly_detail (id, category, location) values (3, 'category 3', 'location 3')
go
insert into tc9x_poly_detail (id, category, location) values (4, 'category 4', 'location 4')
go
insert into tc9x_poly_detail (id, category, location) values (5, 'category 5', 'location 5')
go

insert into tc9x_poly_prod (id, name, detail, owner) values (1, 'laptop 1', 1, 1)
go
insert into tc9x_poly_computer (id, cpu) values (1, 'centrino')
go
insert into tc9x_poly_laptop (id, weight, resolution) values (1, 2800, '1280')
go

insert into tc9x_poly_prod (id, name, detail, owner) values (2, 'laptop 2', 2, 2)
go
insert into tc9x_poly_computer (id, cpu) values (2, 'centrino')
go
insert into tc9x_poly_laptop (id, weight, resolution) values (2, 2700, '1024')
go

insert into tc9x_poly_prod (id, name, detail, owner) values (3, 'server 3', 3, 3)
go
insert into tc9x_poly_computer (id, cpu) values (3, 'pentium 4')
go
insert into tc9x_poly_server (id, numberOfCPUs, support) values (3, 4, 3)
go

insert into tc9x_poly_prod (id, name, detail, owner) values (4, 'server 4', 4, 4)
go
insert into tc9x_poly_computer (id, cpu) values (4, 'pentium 4')
go
insert into tc9x_poly_server (id, numberOfCPUs, support) values (4, 16,5)
go

insert into tc9x_poly_prod (id, name, detail, owner) values (5, 'truck 5', 5, 5)
go
insert into tc9x_poly_car (id, kw, make) values (5, 60, 'make 5')
go
insert into tc9x_poly_truck (id, max_weight) values (5, 4)
go

insert into tc9x_poly_prod_multi (id1, id2, name) values (1, 1, 'laptop 1')
go
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (1, 1, 'centrino')
go
insert into tc9x_poly_laptop_multi (id1, id2, weight, resolution) values (1, 1, 2800, '1280')
go

insert into tc9x_poly_prod_multi (id1, id2, name) values (2, 2, 'laptop 2')
go
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (2, 2, 'centrino')
go
insert into tc9x_poly_laptop_multi (id1, id2, weight, resolution) values (2, 2, 2700, '1024')
go

insert into tc9x_poly_prod_multi (id1, id2, name) values (3, 3, 'server 3')
go
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (3, 3, 'pentium 4')
go
insert into tc9x_poly_server_multi (id1,  id2, numberOfCPUs, support) values (3, 3, 4, 3)
go

insert into tc9x_poly_prod_multi (id1, id2, name) values (4, 4, 'server 4')
go
insert into tc9x_poly_computer_multi (id1, id2, cpu) values (4, 4, 'pentium 4')
go
insert into tc9x_poly_server_multi (id1, id2, numberOfCPUs, support) values (4, 4, 16,5)
go

insert into tc9x_poly_owner (id, name, product) values (1, 'owner 1', 1)
go
insert into tc9x_poly_owner (id, name, product) values (2, 'owner 2', 2)
go
insert into tc9x_poly_owner (id, name, product) values (3, 'owner 3', 3)
go
insert into tc9x_poly_owner (id, name, product) values (4, 'owner 4', 4)
go
insert into tc9x_poly_owner (id, name, product) values (5, 'owner 5', 5)
go

insert into tc9x_poly_ordr (id, name) values (1, 'order 1')
go

insert into tc9x_poly_order_product (order_id, product_id) values (1, 1)
go
insert into tc9x_poly_order_product (order_id, product_id) values (1, 2)
go

insert into tc9x_poly_m_n (m_id, n_id) values (1, 1)
go
insert into tc9x_poly_m_n (m_id, n_id) values (1, 2)
go

insert into tc9x_poly_table_m (id, name) values (1, "m1")
go
insert into tc9x_poly_table_m (id, name) values (2, "m2")
go

insert into tc9x_poly_table_n (id, name) values (1, "n1")
go
insert into tc9x_poly_table_n (id, name) values (2, "n2")
go

drop tabel if exists tc9x_poly_base
go
create table tc9x_poly_base (
  id varchar(64) not null default '',
  color varchar(64) default null,
  primary key  (ID)
)
go

insert into tc9x_poly_base values ('100','red')
go

drop table tc9x_poly_derived
go
create table tc9x_poly_derived (
  id varchar(64) not null default '',
  scent varchar(64) default null,
  primary key  (ID)
)
go
insert into tc9x_poly_derived values ('100','vanilla')
go

drop table tc9x_poly_container
go
create table tc9x_poly_container (
  id varchar(64) not null default '',
  reference varchar(64) default null,
  primary key  (ID)
)
go
insert into tc9x_poly_container values ('200','100')
go

drop table tc9x_poly_Product
go
create table tc9x_poly_Product(
  IdProd int primary key,
  NameProd   varchar(30) null,
  DescProd   varchar(30) null
)
go

drop table tc9x_poly_ActProduct
go
create table tc9x_poly_ActProduct(
  IdAct int primary key references Product (IdProd),
  BestSeason varchar(30) null
)
go

drop table tc9x_poly_ComposedOffer
go
create table tc9x_poly_ComposedOffer(
  IdCOffer numeric(10) primary key references Product (IdProd),
  NameCO   varchar(30) null,
  DescCO   varchar(30) null
)
go

drop table tc9x_poly_OfferComposition
go
create table tc9x_poly_OfferComposition(
  Offer numeric(10),
  Product numeric(10), 
  constraint unique_rel unique (Offer, Product) 
)
go

-- tables required for TestPolymorphismDependedndObject

DROP TABLE tc9x_poly_extend_object
go
CREATE TABLE tc9x_poly_extend_object (
  id            int NOT NULL default '0',
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
)
go

INSERT INTO tc9x_poly_extend_object VALUES (1, 'This is the extended object.')
go

DROP TABLE tc9x_poly_base_object
go
CREATE TABLE tc9x_poly_base_object (
  id           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
)
go

INSERT INTO tc9x_poly_base_object VALUES (1, 'This is the test object.', '0')
go

DROP TABLE tc9x_poly_depend_object
go
CREATE TABLE tc9x_poly_depend_object (
  id           int NOT NULL default '0',
  parentId           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
)
go

INSERT INTO tc9x_poly_depend_object VALUES(1, 1, 'This is a description');
    
# TC20x - self-referential relations 
 
drop table tc200_self_relation_folder
go
create table tc200_self_relation_folder (
  id          int                 not null,  
  name        varchar(255)        not null,
  parent_id      int                DEFAULT null
)
go

drop table tc200_self_relation_parent
go
create table tc200_self_relation_parent (
  id          int                 not null,  
  name        varchar(255)        not null
)
go

drop table tc200_self_relation_extend
go
create table tc200_self_relation_extend (
  id          int                 not null,  
  parent_id      int                DEFAULT null
)
go
    
