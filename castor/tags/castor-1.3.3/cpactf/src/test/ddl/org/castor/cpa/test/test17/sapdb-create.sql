create table test17_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
//

create unique index test17_sample_pk on test17_sample ( id )
//
