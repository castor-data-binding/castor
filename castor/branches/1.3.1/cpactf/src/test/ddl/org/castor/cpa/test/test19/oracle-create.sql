create table test19_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
/

create unique index test19_sample_pk on test19_sample ( id )
/