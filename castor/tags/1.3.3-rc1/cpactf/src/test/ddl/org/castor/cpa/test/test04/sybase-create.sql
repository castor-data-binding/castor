create table test04_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
go

create unique index test04_sample_pk on test04_sample ( id )
go
