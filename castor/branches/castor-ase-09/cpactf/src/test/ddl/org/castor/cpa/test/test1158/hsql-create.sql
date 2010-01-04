CREATE TABLE test1158_extended (
  id            int NOT NULL,
  description2  varchar(50) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO test1158_extended VALUES (1, 'This is the extended object.');

CREATE TABLE test1158_object (
  id           int NOT NULL,
  description  varchar(50) NOT NULL,
  saved        char(1),
  PRIMARY KEY (id)
);

INSERT INTO test1158_object VALUES (1, 'This is the test object.', '');
