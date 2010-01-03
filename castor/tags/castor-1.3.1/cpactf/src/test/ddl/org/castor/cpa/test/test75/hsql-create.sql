create table test75_group (
  gid        int           not null,
  value1     varchar(100)  not null
);
create unique index test75_group_pk on test75_group ( gid );

create table test75_person (
  pid        int           not null,
  value1     varchar(100)  not null,
  helloworld varchar(100)  null,
  sthelse    varchar(100)  null
);
create unique index test75_person_pk on test75_person ( pid );

create table test75_group_person (
  gid        int           not null,
  pid        int           not null 
);
create index test75_group_person_pk1 on test75_group_person ( pid );
create index test75_group_person_pk2 on test75_group_person ( gid );


