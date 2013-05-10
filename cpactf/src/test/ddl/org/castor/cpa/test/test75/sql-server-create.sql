create table test75_group (
  gid        int           not null,
  value1     varchar(100)  not null
)
go
create unique index test75_group_pk on test75_group ( gid )
go

create table test75_group (
  pid        int           not null,
  value1     varchar(100)  not null,
  helloworld varchar(100)  null,
  sthelse    varchar(100)  null
)
go
create unique index test75_group_pk on test75_group ( pid )
go

create table test75_group_person (
  gid        int          not null,
  pid        int          not null,
  CONSTRAINT test75_person_fk FOREIGN KEY (pid) REFERENCES test75_person (pid),
  CONSTRAINT test75_group_fk FOREIGN KEY (gid) REFERENCES test75_group (gid)
)
go
create index test75_person_idx on test75_group_person (pid)
go
create index test75_group_idx on test75_group_person (gid)
go
