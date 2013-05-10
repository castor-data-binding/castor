create sequence test20_keygen_seq
/

create table test20_keygen (
  id    int           not null,
  attr  varchar(200)  not null
)
/

create unique index test20_keygen_pk on test20_keygen ( id )
/

create table test20_keygen_ext (
  id   int          not null,
  ext  varchar(200) not null
)
/

create unique index test20_keygen_ext_pk on test20_keygen_ext ( id )
/

create sequence test20_keygen_string_seq
/

create table test20_keygen_string (
  id    varchar(200)  not null,
  attr  varchar(200)  not null
)
/

create unique index test20_keygen_string_pk on test20_keygen_string ( id )
/

create table test20_keygen_ext_string (
  id   varchar(200) not null,
  ext  varchar(200) not null
)
/

create unique index test20_keygen_ext_string_pk on test20_keygen_ext_string ( id )
/

create table test20_uuid (
  id    char(30)      not null,
  attr  varchar(200)  not null
)
/

create unique index test20_uuid_pk on test20_uuid ( id )
/

create table test20_uuid_ext (
  id   char(30)     not null,
  ext  varchar(200) not null
)
/

create unique index test20_uuid_ext_pk on test20_uuid_ext ( id )
/

create table test20_seqtable (
  table_name  varchar(200)  not null,
  max_id      int
)
/

create unique index test20_seqtable_pk on test20_seqtable ( table_name )
/

create table test20_trigger (
  id    int           not null,
  attr  varchar(200)  not null
)
/
create unique index test20_trigger_pk on test20_trigger ( id )
/

create table test20_trigger_ext (
  id   int          not null,
  ext  varchar(200) not null
)
/
create unique index test20_trigger_ext_pk on test20_trigger_ext ( id )
/

create sequence test20_trigger_seq
/
create trigger test20_trigger_trg
before insert on test20_trigger
for each row
DECLARE 
    iCounter test20_trigger.id%TYPE; 
    cannot_change_counter EXCEPTION; 
BEGIN 
    IF INSERTING THEN 
        select test20_trigger_seq.nextval into iCounter from Dual; 
        :new.ID := iCounter; 
    END IF; 
EXCEPTION 
     WHEN cannot_change_counter THEN 
         raise_application_error(-20000, 'Cannot Change Counter Value'); 
END;
/
