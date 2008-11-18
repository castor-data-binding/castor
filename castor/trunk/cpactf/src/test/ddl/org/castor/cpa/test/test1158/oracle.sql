DROP TABLE test1158_extended CASCADE CONSTRAINTS;

CREATE TABLE test1158_extended (
  id            int NOT NULL,
  description2  varchar(50) NOT NULL
);

ALTER TABLE test1158_extended ADD PRIMARY KEY (id);

INSERT INTO test1158_extended VALUES (1, 'This is the extended object.');


DROP TABLE test1158_object CASCADE CONSTRAINTS;

CREATE TABLE test1158_object (
  id           int NOT NULL,
  description  varchar(50) NOT NULL,
  saved        char(1)
);

ALTER TABLE test1158_object ADD PRIMARY KEY (id);

INSERT INTO test1158_object VALUES (1, 'This is the test object.', ' ');
