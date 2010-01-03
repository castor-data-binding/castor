create table test10_handling (
  id             numeric(10,0)  not null,
  int_val        integer        null,
  float_val      float          null, 
  real_val       real           null,
  long_val       bigint         null,
  char_val       char(1)        null,
  bool_val       char(1)        null,
  bool_is_method char(1)        null,
  int_date       integer        null,
  str_time       char(23)       null,
  num_date       bigint         null
);

create unique index test10_handling_pk on test10_handling ( id );
