drop table if exists soakEmployee;

create table soakEmployee (
    id                           int            not null primary key auto_increment,
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
    homeAddress                  BINARY         null,
    homePhone                    BINARY         null,
    skills                       BINARY         null
);

create unique index soakEmployee_PK on soakEmployee(id);
