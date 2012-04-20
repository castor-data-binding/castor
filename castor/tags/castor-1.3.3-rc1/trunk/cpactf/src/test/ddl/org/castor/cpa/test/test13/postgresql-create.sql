create table test13_serial (
  id      integer        not null,
  dep     bytea          null
);
create unique index test13_serial_pk on test13_serial( id );
