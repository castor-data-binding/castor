drop table if exists PTF_LOCKED;

create table PTF_LOCKED ( 
  ID                        int not null, 
  NAME                      varchar(40) not null, 
  INPUT                     varchar(1) not null, 
  OUTPUT                    varchar(1) not null
);

create unique index PTF_LOCKED_PK on PTF_LOCKED ( ID );

insert into PTF_LOCKED values (1, 'Not locked', 'N', 'N');
insert into PTF_LOCKED values (2, 'Totally locked', 'Y', 'Y');
insert into PTF_LOCKED values (3, 'Input locked', 'Y', 'N');
insert into PTF_LOCKED values (4, 'Output locked', 'N', 'Y');


drop table if exists PTF_STATE;

create table PTF_STATE ( 
  ID                        int not null, 
  NAME                      varchar(25) not null, 
  LOCKED_ID                 int not null, 
  INPUT                     int not null, 
  OUTPUT                    int not null, 
  SERVICE                   int not null, 
  CHANGE_FROM               int not null, 
  CHANGE_TO                 int not null, 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_STATE_PK on PTF_STATE ( ID );



drop table if exists PTF_DEPARTMENT;

create table PTF_DEPARTMENT ( 
  ID                        int not null, 
  NAME                      varchar(25) not null, 
  DESCRIPTION               varchar(100), 
  STATE_ID                  int not null, 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_DEPARTMENT_PK on PTF_DEPARTMENT ( ID );


drop table if exists PTF_SUPPLIER;

create table PTF_SUPPLIER ( 
  ID                        int not null, 
  NAME                      varchar(25) not null, 
  DESCRIPTION               varchar(100), 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_SUPPLIER_PK on PTF_SUPPLIER ( ID );


drop table if exists PTF_REASON;

create table PTF_REASON ( 
  ID                        int not null, 
  NAME                      varchar(25) not null, 
  DESCRIPTION               varchar(100), 
  FAILURE                   int not null, 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_REASON_PK on PTF_REASON ( ID );


drop table if exists PTF_TYPE;

create table PTF_TYPE ( 
  ID                        int not null, 
  NUMBER                    varchar(25) not null, 
  DESCRIPTION               varchar(100), 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_TYPE_PK on PTF_TYPE ( ID );


drop table if exists PTF_EQUIPMENT;

create table PTF_EQUIPMENT ( 
  ID                        int not null, 
  TYPE_ID                   int not null, 
  NUMBER                    varchar(25) not null, 
  DESCRIPTION               varchar(100), 
  SUPPLIER_ID               int not null, 
  DELIVERY                  int not null, 
  COST                      float not null, 
  SERIAL                    varchar(50), 
  STATE_ID                  int not null, 
  REASON_ID                 int not null, 
  COUNT                     int not null, 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_EQUIPMENT_PK on PTF_EQUIPMENT ( ID );


drop table if exists PTF_SERVICE;

create table PTF_SERVICE ( 
  ID                        int not null, 
  EQUIPMENT_ID              int not null, 
  NUMBER                    int not null, 
  NAME                      varchar(25) not null, 
  DESCRIPTION               varchar(100), 
  DATE                      varchar(25) not null, 
  FLAG_1                    int not null, 
  FLAG_2                    int not null, 
  FLAG_3                    int not null, 
  FLAG_4                    int not null, 
  NOTE                      varchar(2000), 
  CRD                       varchar(25) not null, 
  CRM                       varchar(25) not null, 
  UPD                       varchar(25) not null, 
  UPM                       varchar(25) not null
);

create unique index PTF_SERVICE_PK on PTF_SERVICE ( ID );
