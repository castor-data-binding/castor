CREATE TABLE test99_poly_product(
  idprod int not null PRIMARY KEY,
  nameprod VARCHAR(30) default NULL,
  descprod VARCHAR(30) default NULL
);

CREATE TABLE test99_poly_actproduct(
  idact int not null PRIMARY KEY REFERENCES test99_poly_product (idprod),
  bestseason VARCHAR(30) default NULL
);

CREATE TABLE test99_poly_composedoffer(
  idcoffer int not null PRIMARY KEY REFERENCES test99_poly_product (idprod),
  nameco VARCHAR(30) default NULL,
  descco VARCHAR(30) default NULL
);

CREATE TABLE test99_poly_offercomposition(
  offer NUMERIC(10) not null,
  product NUMERIC(10) not null, 
  CONSTRAINT unique_rel UNIQUE (offer, product)
);
