drop table prod;
create table prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null,
  group_id  int           not null
);

drop table computer;
create table computer (
  id   int          not null,
  cpu  varchar(200) not null
);

drop table category_prod;
create table category_prod (
  prod_id       int not null,
  category_id   int not null
);
