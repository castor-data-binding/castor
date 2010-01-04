CREATE TABLE test96_poly_extend_object (
  id            int NOT NULL,
  description2  varchar(50) NOT NULL,
  PRIMARY KEY (id)
)
/

INSERT INTO test96_poly_extend_object VALUES (1, 'This is the extended object.')
/

CREATE TABLE test96_poly_base_object (
  id           int NOT NULL,
  description  varchar(50) NOT NULL,
  saved        char(1),
  PRIMARY KEY (id)
)
/

INSERT INTO test96_poly_base_object VALUES (1, 'This is the test object.', '0')
/

CREATE TABLE test96_poly_depend_object (
  id           int NOT NULL,
  parentId           int NOT NULL,
  description  varchar(50) NOT NULL,
  PRIMARY KEY (id)
)
/

INSERT INTO test96_poly_depend_object VALUES(1, 1, 'This is a description')
/
