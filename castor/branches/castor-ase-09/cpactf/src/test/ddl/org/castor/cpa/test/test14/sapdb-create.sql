create table test14_rollback (
  id      fixed(10,0)   not null,
  value1  varchar(200)  not null,
  value2  varchar(200),
  numb    fixed(10,0)
)
//
create unique index test14_rollback_pk on test14_rollback ( id )
//
