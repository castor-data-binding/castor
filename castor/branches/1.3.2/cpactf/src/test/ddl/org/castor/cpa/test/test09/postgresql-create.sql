create table test09_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
);

create unique index test09_sample_pk on test09_sample ( id );
