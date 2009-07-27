drop table if exists test954_prod;

create table test954_prod (
  id        int           not null,
  name      varchar(200)  not null
);

insert into test954_prod values (1, 'This is the test object.');
