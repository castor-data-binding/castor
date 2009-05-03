drop table test11_lob
go
create table test11_lob (
  id         numeric(10,0)  not null,
  blob_val   image          null,
  clob_val   text           null,
  clob_val2  text           null
)
go
create unique index test11_lob_pk on test11_lob ( id )
go
grant all on test11_lob to test
go
