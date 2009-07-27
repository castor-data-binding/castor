DROP TABLE test2177_entity CASCADE CONSTRAINTS;

CREATE TABLE test2177_entity (
  id        int not null,
  name      varchar(200) not null
);

INSERT INTO test2177_entity (id, name) VALUES (1, 'entity1');
