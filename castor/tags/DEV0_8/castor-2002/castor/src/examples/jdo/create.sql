create table prod (
  id        int not null,
  name      varchar(200) not null,
  price     float not null,
  group_id  int not null
);

create unique index prod_pk on prod ( id );


create table prod_group (
  id    int not null,
  name  varchar(200) not null
);

create unique index prod_group_pk on prod_group ( id );


create table prod_detail (
  id      int not null,
  prod_id int not null,
  name    varchar(200) not null
);

create unique index prod_detail_pk on prod_detail ( id );


create table computer (
  id   int not null,
  cpu  varchar(200) not null
);

create unique index computer_pk on computer ( id );


create table category (
  id   int not null,
  name varchar(200) not null
);

create unique index category_pk on category ( id );


create table category_prod (
  prod_id   int not null,
  category_id   int not null
);

create unique index category_prod_pk on category_prod ( prod_id, category_id );




