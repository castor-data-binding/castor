drop table prod;
create table prod (
  id        int not null,
  name      varchar(200) not null,
  price     numeric(18,2) not null,
  group_id  int not null
);
create unique index prod_pk on prod ( id );


drop table prod_group;
create table prod_group (
  id    int not null,
  name  varchar(200) not null
);
create unique index prod_group_pk on prod_group ( id );


drop table prod_detail;
create table prod_detail (
  id      int not null,
  prod_id int not null,
  name    varchar(200) not null
);
create unique index prod_detail_pk on prod_detail ( id );


drop table computer;
create table computer (
  id   int not null,
  cpu  varchar(200) not null
);
create unique index computer_pk on computer ( id );


drop table category;
create table category (
  id   int not null,
  name varchar(200) not null
);
create unique index category_pk on category ( id );


drop table category_prod;
create table category_prod (
  prod_id   int not null,
  category_id   int not null
);
create unique index category_prod_pk on category_prod ( prod_id, category_id );




