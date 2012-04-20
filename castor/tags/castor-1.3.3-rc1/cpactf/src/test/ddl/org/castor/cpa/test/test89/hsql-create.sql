create table test89_pks_person (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  bday  datetime null 
);

create unique index test89_pks_per_pk on test89_pks_person( fname, lname );

create table test89_pks_employee (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  start_date datetime null
);

create unique index test89_pks_per_emp_pk on test89_pks_employee( fname, lname );

create table test89_pks_payroll (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  holiday int,
  hourly_rate int
);

create unique index test89_pks_pay_fk on test89_pks_payroll( fname, lname );

create unique index test89_pks_pay_pk on test89_pks_payroll( id );

create table test89_pks_project (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id    int             not null,
  name  varchar(100)
);

create unique index test89_pks_pro_pk on test89_pks_project( id );

create table test89_pks_address (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  id int               not null,
  street varchar(30)    ,
  city  varchar(30)    ,
  state varchar(2)     ,
  zip varchar(6)       
);

create unique index test89_pks_add_pk on test89_pks_address( id );

create table test89_pks_contract (
  fname varchar(100)    not null,
  lname varchar(100)    not null,
  policy_no int        not null,
  contract_no int      not null,
  c_comment varchar(100)  
);

create unique index test89_pks_cont_fk on test89_pks_contract( fname, lname );

create unique index test89_pks_cont_pk on test89_pks_contract( policy_no, contract_no );

create table test89_pks_category_contract (
  policy_no int        not null,
  contract_no int      not null,
  cate_id int          not null
);

create table test89_pks_category (
  id  int              not null,
  name varchar(100)     not null
);

create unique index test89_pks_cat_pk on test89_pks_category( id );
