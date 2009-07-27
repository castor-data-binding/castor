drop table if exists test1379_prod;

create table test1379_prod (
  id        int           not null,
  name      varchar(200)  not null,
  price     numeric(18,2) not null
);

drop table if exists test1379_computer;

create table test1379_computer (
  id   int          not null,
  cpu  varchar(200) not null
);
