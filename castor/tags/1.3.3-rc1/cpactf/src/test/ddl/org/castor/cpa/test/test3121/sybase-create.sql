create table test3121_sample (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
go

create unique index test3121_sample_pk on test3121_sample ( id )
go
