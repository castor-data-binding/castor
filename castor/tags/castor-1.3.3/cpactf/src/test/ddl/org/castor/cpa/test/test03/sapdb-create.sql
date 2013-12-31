create table test03_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
//

create unique index test03_sample_pk on test03_sample ( id )
//
