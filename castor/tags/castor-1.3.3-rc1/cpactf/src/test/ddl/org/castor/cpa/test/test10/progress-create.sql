create table test10_handling (
  id             numeric(10,0)  not null,
  int_val        integer,
  float_val      float,
  real_val       real,
  long_val       numeric(18,0),
  char_val       char(1),
  bool_val       char(1),
  bool_is_method char(1),
  int_date       integer,
  str_time       char(23),
  num_date       numeric(17,0)
);
create unique index test10_handling_pk on test10_handling ( id );
