drop table test20_keygen;
drop sequence test20_keygen_seq;

drop table test20_keygen_ext;

drop table test20_keygen_string;
drop sequence test20_keygen_string_seq;

drop table test20_keygen_ext_string;

drop table test20_uuid;

drop table test20_uuid_ext;

-- Note that 7.2.1 requires the following sequence to be dropped,
-- where as 7.3 version of postgresql will drop it automatically
drop table test20_identity;
drop sequence test20_identity_id_seq;

drop table test20_identity_ext;

drop table test20_seqtable;

drop trigger test20_trigger_trg ON test20_trigger;
drop function test20_trigger_prcd();
drop sequence test20_trigger_seq;
drop table test20_trigger_ext;
drop table test20_trigger;

