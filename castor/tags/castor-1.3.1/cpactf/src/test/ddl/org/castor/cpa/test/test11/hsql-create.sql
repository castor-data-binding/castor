create table test11_lob (
  id        numeric(10,0)      not null,
  blob_val  longvarbinary      null,
  clob_val  longvarchar        null,
  clob_val2 longvarchar        null
);
create unique index test11_lob_pk on test11_lob ( id );
