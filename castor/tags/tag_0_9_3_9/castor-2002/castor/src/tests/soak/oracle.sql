drop table soakEmployee;
create table soakEmployee (
    id                           int not        null,
    fname                        varchar(30)    null,
    lname                        varchar(30)    null,
    mi                           char           null,
    gender                       char           null,
    isPerm                       numeric(1,0)   null,
    hourlyRate                   numeric(20,2)  null,
    totalHours                   numeric(20,2)  null,
    SSN                          numeric(9,0)   null,
    holiday                      float          null,
    startDate                    date           null,
    recordDate                   date           null,
    roomNumber                   numeric(4,0)   null,
    homeAddress                  RAW (2000)     null,
    homePhone                    RAW (2000)     null,
    skills                       RAW (2000)     null
);

create unique index soakEmployee_PK on soakEmployee ( id );

drop table   test_seqtable;

create table test_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
);

create unique index test_seqtable_pk
  on test_seqtable ( table_name );


drop sequence   soakEmployee_seq;

create sequence soakEmployee_seq;
