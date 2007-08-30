drop table if exists prod;
create table prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null
);

drop table if exists computer;

create table computer (
  id   int          not null,
  cpu  varchar(200) not null
);
