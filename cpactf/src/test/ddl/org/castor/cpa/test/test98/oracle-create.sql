CREATE TABLE test98_poly_base (
  id varchar(64) NOT NULL,
  color varchar(64),
  PRIMARY KEY (id)
)
/

INSERT INTO test98_poly_base VALUES ('100','red')
/

CREATE TABLE test98_poly_derived (
  id varchar(64) NOT NULL,
  scent varchar(64),
  PRIMARY KEY (id)
)
/

INSERT INTO test98_poly_derived VALUES ('100','vanilla')
/

CREATE TABLE test98_poly_container (
  id varchar(64) NOT NULL,
  reference varchar(64),
  PRIMARY KEY (id)
)
/

INSERT INTO test98_poly_container VALUES ('200','100')
/
