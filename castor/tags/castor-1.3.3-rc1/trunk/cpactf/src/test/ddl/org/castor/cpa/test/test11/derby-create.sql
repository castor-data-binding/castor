create table test11_lob (
  id        numeric(10,0)  not null,
  blob_val  blob,
  clob_val  clob,
  clob_val2 clob
);

create unique index test11_lob_pk on test11_lob ( id );
