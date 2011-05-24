create table test10_handling (
  id              fixed(10,0)  not null,
  int_val         integer      null,
  float_val       float        null,
  real_val        float        null,
  long_val        fixed(18,0)  null,
  char_val        char(1)      null,
  bool_val        char(1)      null,
  bool_is_method  char(1)      null,
  int_date        integer      null,
  str_time        char(24)     null,
  num_date        fixed(17,0)  null
)
//
create unique index test10_handling_pk on test10_handling ( id )
//
