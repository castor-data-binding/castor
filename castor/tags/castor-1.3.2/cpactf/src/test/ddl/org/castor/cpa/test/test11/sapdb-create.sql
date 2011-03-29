create table test11_lob (
  id         fixed(10,0)    not null,
  blob_val   long byte      null,
  clob_val   long           null,
  clob_val2  long           null
)
//
create unique index test11_lob_pk on test11_lob ( id )
//
