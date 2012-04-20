create table test14_rollback (
  id      numeric(10,0) not null,
  value1  varchar(200)  not null,
  value2  varchar(200),
  numb    numeric(10,0)
);
create unique index test14_rollback_pk on test14_rollback ( id );
