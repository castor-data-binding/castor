drop table test13_serial;

create table test13_serial (
  id      integer        not null,
  dep     longvarbinary  null
);

create unique index test13_serial_pk on test13_serial( id );
