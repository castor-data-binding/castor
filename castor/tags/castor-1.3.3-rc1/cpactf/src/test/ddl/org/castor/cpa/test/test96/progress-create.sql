CREATE TABLE test96_poly_extend_object (
  id            int NOT NULL default 0,
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO test96_poly_extend_object VALUES (1, 'This is the extended object.');

CREATE TABLE test96_poly_base_object (
  id           int NOT NULL default 0,
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
);

INSERT INTO test96_poly_base_object VALUES (1, 'This is the test object.', '0');

CREATE TABLE test96_poly_depend_object (
  id           int NOT NULL default 0,
  parentId           int NOT NULL default 0,
  description  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
);

INSERT INTO test96_poly_depend_object VALUES(1, 1, 'This is a description');
