CREATE TABLE test2567_entity(
	partname varchar(29),
	origdatereqd date,
	custordernumber varchar(20),
	loaddate date
)
GO

INSERT INTO test2567_entity (
    partname, origdatereqd, custordernumber, loaddate
) VALUES (
    'PART-30021', '2005-01-03', '12701', '2004-04-26'
)
GO
