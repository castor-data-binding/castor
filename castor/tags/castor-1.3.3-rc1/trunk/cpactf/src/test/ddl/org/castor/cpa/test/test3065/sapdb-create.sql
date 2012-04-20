CREATE TABLE test3065_extended (
  id            int NOT NULL,
  description2  varchar(50) NOT NULL,
  PRIMARY KEY (id)
)
//

INSERT INTO test3065_extended VALUES (1, 'This is the extended object.')
//

CREATE TABLE test3065_extendedextended (
  id            int NOT NULL,
  description3  varchar(50) NOT NULL,
  PRIMARY KEY (id)
)
//

INSERT INTO test3065_extendedextended VALUES (1, 'This is the extendedextended object.')
//

CREATE TABLE test3065_extended2 (
  id            int NOT NULL,
  description2  varchar(50) NOT NULL,
  PRIMARY KEY (id)
)
//

INSERT INTO test3065_extended2 VALUES (2, 'This is the extended object2.')
//

CREATE TABLE test3065_object (
  id           int NOT NULL,
  description  varchar(50) NOT NULL,
  saved        char(1),
  PRIMARY KEY (id)
)
//

INSERT INTO test3065_object VALUES (1, 'This is the test object1.', '0')
//
INSERT INTO test3065_object VALUES (2, 'This is the test object2.', '0')
//