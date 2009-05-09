create table test13_serial (
  id      integer        not null,
  dep     image          null
)
go
create unique index test13_serial_pk on test13_serial( id )
go
