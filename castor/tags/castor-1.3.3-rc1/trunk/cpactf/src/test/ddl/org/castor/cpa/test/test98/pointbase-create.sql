CREATE TABLE test98_poly_base (
  id varchar(64) NOT NULL default '',
  color varchar(64),
  PRIMARY KEY  (ID)
) ;

INSERT INTO test98_poly_base VALUES ('100','red');

CREATE TABLE test98_poly_derived (
  id varchar(64) NOT NULL default '',
  scent varchar(64),
  PRIMARY KEY  (ID)
) ;

INSERT INTO test98_poly_derived VALUES ('100','vanilla');

CREATE TABLE test98_poly_container (
  id varchar(64) NOT NULL default '',
  reference varchar(64),
  PRIMARY KEY  (ID)
) ;

INSERT INTO test98_poly_container VALUES ('200','100');
