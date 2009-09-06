DROP TABLE test356_entity CASCADE CONSTRAINTS;

CREATE TABLE test356_entity (
  id       int not null,
  prop     int
);

ALTER TABLE test356_entity ADD PRIMARY KEY (id);

INSERT INTO test356_entity (id, prop) VALUES (1, 100);
INSERT INTO test356_entity (id, prop) VALUES (2, 200);
INSERT INTO test356_entity (id, prop) VALUES (3, 300);
INSERT INTO test356_entity (id, prop) VALUES (4, 400);
INSERT INTO test356_entity (id, prop) VALUES (5, null);
