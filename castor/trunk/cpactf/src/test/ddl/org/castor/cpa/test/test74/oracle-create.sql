create table test74_many_group (
  gid       int           not null primary key,
  value1    varchar(100)  not null
)
/

create table test74_many_person (
   pid      int          not null primary key,
   value1   varchar(100) not null,
   helloworld varchar(100) ,
   sthelse varchar(100) 
)
/

create table test74_group_person (
  gid int         not null,
  pid int        not null,
  CONSTRAINT test74_person_delete
    FOREIGN KEY(pid) 
    REFERENCES test74_many_person(pid),
  CONSTRAINT test74_group_delete
    FOREIGN KEY(gid) 
    REFERENCES test74_many_group(gid)
)
/
