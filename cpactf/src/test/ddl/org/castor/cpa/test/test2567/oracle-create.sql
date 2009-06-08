CREATE TABLE test2567_entity (
    partname            VARCHAR2 (29)    NULL, 
    origdatereqd        DATE             NULL, 
    custordernumber     VARCHAR2 (20)    NULL, 
    loaddate            DATE             NULL
)
/

INSERT INTO test2567_entity (
    partname, origdatereqd, custordernumber, loaddate
) VALUES (
    'PART-30021', to_timestamp('03.01.05','DD.MM.RR HH24:MI:SSXFF'), '12701', to_timestamp('26.04.04','DD.MM.RR HH24:MI:SSXFF')
)
/
