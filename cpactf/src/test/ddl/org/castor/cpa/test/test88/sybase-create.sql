create table test88_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  datetime
)
go

create unique index test88_pks_per_pk on test88_pks_person( fname, lname )
go

create table test88_pks_employee (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  start_date datetime
)
go

create unique index test88_pks_per_emp_pk on test88_pks_employee( fname, lname )
go

create table test88_pks_payroll (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
)
go

create unique index test88_pks_pay_fk on test88_pks_payroll( fname, lname )
go

create unique index test88_pks_pay_pk on test88_pks_payroll( id )
go

create table test88_pks_project (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id    int             not null,
  name  varchar(100)
)
go

create unique index test88_pks_pro_pk on test88_pks_project( id )
go

create table test88_pks_address (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  street varchar(30)    ,
  city  varchar(30)    ,
  state varchar(2)     ,
  zip varchar(6)       
)
go

create unique index test88_pks_add_pk on test88_pks_address( id )
go

create table test88_pks_contract (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(100)  
)
go

create unique index test88_pks_cont_fk on test88_pks_contract( fname, lname )
go

create unique index test88_pks_cont_pk on test88_pks_contract( policy_no, contract_no )
go

create table test88_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
)
go

create table test88_pks_category (
  id  int              not null,
  name varchar(100)     not null
)
go

create unique index test88_pks_cat_pk on test88_pks_category( id )
go

create table test88_nton_a (
  id         varchar(20)      not null,
  status     int              not null
)
go

create table test88_nton_b (
  id         varchar(20)      not null,
  status     int              not null
)
go