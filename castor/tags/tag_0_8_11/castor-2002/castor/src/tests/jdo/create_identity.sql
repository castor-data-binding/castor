create table test_identity (
  id numeric(10,0) identity,
  attr varchar(200) not null,
);

create table test_identity_ext (
  id numeric(10,0) not null,
  ext varchar(200) not null,
);

create unique index test_identity_ext_pk on test_identity_ext ( id );
