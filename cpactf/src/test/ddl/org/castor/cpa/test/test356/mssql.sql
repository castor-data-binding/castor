DROP TABLE test356_entity
GO

CREATE TABLE test356_entity(
	id int NOT NULL,
	prop int,
PRIMARY KEY ( id )
)
GO

INSERT INTO test356_entity (id, prop) VALUES (1, 100);
INSERT INTO test356_entity (id, prop) VALUES (2, 200);
INSERT INTO test356_entity (id, prop) VALUES (3, 300);
INSERT INTO test356_entity (id, prop) VALUES (4, 400);
INSERT INTO test356_entity (id, prop) VALUES (5, NULL);
