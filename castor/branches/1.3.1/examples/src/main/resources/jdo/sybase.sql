drop table computer
go
drop table prod_detail
go
drop table prod
go
drop table prod_group
go


create table prod_group (
  id    int           not null primary key,
  name  varchar(200)  not null
)
go
grant all on prod_group to test
go

create table prod (
  id        int            not null primary key,
  name      varchar(200)   not null,
  price     numeric(18,2)  not null,
  group_id  int            not null references prod_group( id )
)
go
grant all on prod to test
go

create table prod_detail (
  id       int           not null primary key,
  prod_id  int           not null references prod( id ),
  name     varchar(200)  not null
)
go
grant all on prod_detail to test
go

create table computer (
  id   int           not null primary key references prod( id ),
  cpu  varchar(200)  not null
)
go
grant all on computer to test
go


drop table prod_cat
go
create table prod_cat (
  id   int not null primary key,
  name varchar(200) not null
)
go

drop table prod_cat_prod
go
create table prod_cat_prod (
  prod_id   int not null,
  category_id   int not null
)
go
create unique index prod_cat_prod_pk on prod_cat_prod ( prod_id, category_id )
go




