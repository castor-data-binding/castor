dump tran master with truncate_only
go
set quoted_identifier on
go


-- DROP TABLES --

-- Drop purchase order tables
drop table oes_line_item
go
drop table oes_pur_order
go
-- Drop product inventory tables
drop table oes_product
go
drop table oes_category
go
drop table oes_discount
go
drop table oes_price
go
-- Drop code tables
drop table oes_order_status_code
go
drop table oes_ship_term_code
go
drop table oes_fin_term_code
go
drop table oes_cur_unit_code
go
drop table oes_country_code
go


-- CODE TABLES --

--
-- Order status code (code table)
--
create table oes_order_status_code (
  code    char(1)  not null  primary key,
  "desc"  varchar  not null
)
go
grant all on oes_order_status_code to oes_user
go

--
-- Shipment terms code (code table)
--
create table oes_ship_term_code (
  code    char(2)  not null  primary key,
  "desc"  varchar  not null
)
go
grant all on oes_ship_term_code to oes_user
go

--
-- Financial terms code (code table)
--
create table oes_fin_term_code (
  code    char(1)  not null  primary key,
  "desc"  varchar  not null
)
go
grant all on oes_fin_term_code to oes_user
go

--
-- Currency unit code (code table)
--
create table oes_cur_unit_code (
  code    char(3)  not null  primary key,
  "desc"  varchar  not null
)
go
grant all on oes_cur_unit_code to oes_user
go

--
-- Country code (code table)
--
create table oes_country_code (
  "code"  char(2)  not null  primary key,
  "desc"  varchar  not null
)
go
grant all on oes_country_code to oes_user
go



-- PRODUCT INVENTORY TABLES --

--
-- Category
--
create table oes_category (
  categ_id  smallint  not null  primary key,
  short     char(20)  not null,
  "desc"    varchar   not null
)
go
grant all on oes_category to oes_user
go

--
-- Price
--
create table oes_price (
  price_id  numeric(10,0)  identity  primary key,
  msrp      numeric(15,2)  not null,
  cost      numeric(15,2),
  cur_unit  char(3)        not null  references oes_cur_unit_code (code),
)
go
grant all on oes_price to oes_user
go

--
-- Discount
--
create table oes_discount (
  discount_id  numeric(10,0)  identity  primary key,
  price_id     numeric(10,0)  not null  references oes_price ( price_id ),
  minimum      int            not null,
  discount     int            not null,
  effect_from  datetime       null,
  effect_to    datetime       null
)
go
grant all on oes_discount to oes_user
go

--
-- Product
--
create table oes_product (
  sku       char(14)       not null  primary key,
  short     char(20)       not null  unique,
  "desc"    varchar(200)   not null,
  price_id  numeric(10,0)  not null  references oes_price (price_id),
  categ_id  smallint       not null  references oes_category (categ_id)
)
go
grant all on oes_product to oes_user
go



-- PURCHASE ORDER TABLES --

--
-- PurchaseOrder
--
create table oes_pur_order (
  order_id       numeric(10,0)  identity  primary key,
  status         char(1)        not null  references oes_order_status_code (code),
  comment        varchar        null,
  quote_id       varchar        null,
  packing        varchar        null,
  deliver_to     varchar        not null,
  ship_term      char(2)        not null  references oes_ship_term_code (code),
)
go
grant all on oes_pur_order to oes_user
go

--
-- PurchaseOrder.LineItem
--
create table oes_line_item (
  order_id     numeric(10)    not null  references oes_pur_order (order_id),
  line_no      numeric(5)     not null,
  sku          char(14)       not null  references oes_product (sku),
  quantity     numeric(10)    not null,
  amount       numeric(18,2)  not null,
  cur_unit     char(3)        not null  references oes_cur_unit_code (code),
  ship_date    datetime       null,
  contract_id  varchar        null,
  handling     varchar        null
)
go
create unique index oes_line_item_pk on oes_line_item ( order_id, line_no )
go
grant all on oes_line_item to oes_user
go










