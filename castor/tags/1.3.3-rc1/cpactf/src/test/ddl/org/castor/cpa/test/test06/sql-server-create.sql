create table test06_race (
  id      int          not null,
  value1  int          not null
)
go

create unique index test06_race_pk on test06_race ( id )
go
