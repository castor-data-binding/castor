create table test73_many_group (
  gid       int           not null primary key,
  value1    varchar(100)  not null
)
/

create table test73_many_person (
   pid      int          not null primary key,
   value1   varchar(100) not null,
   helloworld varchar(100) ,
   sthelse varchar(100) 
)
/

create table test73_group_person (
  gid int         not null,
  pid int        not null,
  CONSTRAINT test73_person_delete
    FOREIGN KEY(pid) 
    REFERENCES test73_many_person(pid),
  CONSTRAINT test73_group_delete
    FOREIGN KEY(gid) 
    REFERENCES test73_many_group(gid)
)
/
