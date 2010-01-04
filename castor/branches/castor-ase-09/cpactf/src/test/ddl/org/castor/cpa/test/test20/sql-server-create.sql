create table test20_keygen (
  id    int           not null,
  attr  varchar(200)  not null
)
go

create unique index test20_keygen_pk on test20_keygen ( id )
go

create table test20_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
)
go

create unique index test20_keygen_ext_pk on test20_keygen_ext ( id )
go

create table test20_keygen_string (
  id    varchar(200)  not null,
  attr  varchar(200)  not null
)
go

create unique index test20_keygen_string_pk on test20_keygen_string ( id )
go

create table test20_keygen_ext_string (
  id   varchar(200) not null,
  ext  varchar(200) not null
)
go

create unique index test20_keygen_ext_string_pk on test20_keygen_ext_string ( id )
go

create table test20_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
)
go

create unique index test20_uuid_pk on test20_uuid ( id )
go

create table test20_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
)
go

create unique index test20_uuid_ext_pk on test20_uuid_ext ( id )
go

create table test20_identity (
  id    numeric(10,0) identity,
  attr  varchar(200) not null
)
go

create table test20_identity_ext (
  id   integer not null,
  ext  varchar(200) not null
)
go

create unique index test20_ident_ext_pk on test20_identity_ext ( id )
go

create table test20_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
)
go

create unique index test20_seqtable_pk on test20_seqtable ( table_name )
go