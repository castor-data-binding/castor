drop table test1073_duration;

create table test1073_duration (
  id                 int not null,
  long_duration      number(20,0),
  string_duration    varchar(100)
);

drop table test1073_time;

create table test1073_time (
  id                 int not null,
  long_time_local    number(20,0),
  long_time_utc      number(20,0),
  string_time_local  varchar(100),
  string_time_utc    varchar(100)
);
