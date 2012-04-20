create table test73_group_person (
  gid int         not null,
  pid int        not null 
)
//

create index test73_group_person_p_pk on test73_group_person ( pid )
//

create index test73_group_person_g_pk on test73_group_person ( gid )
//

create table test73_many_group (
  gid       int           not null,
  value1    varchar(100)  not null,
  primary key (gid)
)
//

create table test73_many_person (
   pid      int          not null,
   value1   varchar(100) not null,
   helloworld varchar(100) null,
   sthelse varchar(100) null,
   primary key (pid)
)
//