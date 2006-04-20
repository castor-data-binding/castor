create table test_table (
  id int not null,
  first varchar(200) not null,
  second varchar(200) not null
);

create unique index test_table_pk on test_table ( id );



create table test_master (
  id      int not null,
  value   varchar(200) not null,
  one_of  int not null
);

create unique index test_master_pk on test_master ( id );

create table test_detail (
  detail_id  int not null,
  master_id  int not null,
  value      varchar(200) not null
);

create unique index test_detail_pk on test_detail ( detail_id );

create table test_group (
  id      int not null,
  value   varchar(200) not null
);

create unique index test_group_pk on test_group ( id );

create table test_many (
  id      int not null,
  value   varchar(200) not null
);

create unique index test_many_pk on test_many ( id );

create table test_many_rel (
  master_id  int not null,
  many_id    int not null
);

create unique index test_many_rel_pk on test_many_rel ( master_id, many_id );


