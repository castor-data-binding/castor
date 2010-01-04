create table test11_lob (
  id        numeric(10,0)  not null,
  blob_val  longblob           null,
  clob_val  longtext           null,
  clob_val2 longtext           null
);

create unique index test11_lob_pk on test11_lob ( id );
