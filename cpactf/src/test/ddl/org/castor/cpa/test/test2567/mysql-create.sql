CREATE TABLE test2567_entity (
    partname            VARCHAR (29)    NULL, 
    origdatereqd        DATE            NULL, 
    custordernumber     VARCHAR (20)    NULL, 
    loaddate            DATE            NULL
);

INSERT INTO test2567_entity (
    partname, origdatereqd, custordernumber, loaddate
) VALUES (
    'PART-30021', '2005-01-03', '12701', '2004-04-26'
);
