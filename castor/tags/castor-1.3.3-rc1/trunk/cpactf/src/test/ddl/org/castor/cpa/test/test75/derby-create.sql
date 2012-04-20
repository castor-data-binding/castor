create table test75_group (
  gid          int           not null primary key,
  value1       varchar(100)  not null
);

create table test75_person (
  pid          int           not null primary key,
  value1       varchar(100)  not null,
  helloworld   varchar(100),
  sthelse      varchar(100) 
);

create table test75_group_person (
  gid          int           not null,
  pid          int           not null,
  CONSTRAINT test75_person_fk FOREIGN KEY(pid) REFERENCES test75_person(pid),
  CONSTRAINT test75_group_fk FOREIGN KEY(gid) REFERENCES test75_group(gid)
);

