drop table test11_lob;

create table test11_lob (
  id        numeric(10,0)  not null,
  blob_val  blob           null,
  clob_val  clob           null,
  clob_val2 clob           null
);

create unique index test11_lob_pk on test11_lob ( id );
