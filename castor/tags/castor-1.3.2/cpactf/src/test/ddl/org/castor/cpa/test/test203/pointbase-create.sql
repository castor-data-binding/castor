create table test203_timezone_entity (
  id          int not null,
  name        varchar(200) not null,
  start_date  date,
  start_time  time,
  start_stamp timestamp
);

insert into test203_timezone_entity (id, name) values (1, 'entity1');
