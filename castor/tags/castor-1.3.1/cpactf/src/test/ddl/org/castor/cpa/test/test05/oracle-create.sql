create table test05_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
/

create unique index test05_sample_pk on test05_sample ( id )
/