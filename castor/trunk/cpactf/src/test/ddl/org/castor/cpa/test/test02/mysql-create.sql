create table test02_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
) ENGINE = INNODB;

create unique index test02_sample_pk on test02_sample ( id );
