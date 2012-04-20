create table test01_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
/

create unique index test01_sample_pk on test01_sample ( id )
/