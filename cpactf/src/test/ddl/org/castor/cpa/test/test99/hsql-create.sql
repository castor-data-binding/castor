CREATE TABLE test99_poly_product(
  idprod int not null PRIMARY KEY,
  nameprod VARCHAR(30) default NULL,
  descprod VARCHAR(30) default NULL
);

CREATE TABLE test99_poly_actproduct(
  idact int not null PRIMARY KEY ,
  bestseason VARCHAR(30) default NULL,
  FOREIGN KEY(idact) REFERENCES test99_poly_product (idprod)
);

CREATE TABLE test99_poly_composedoffer(
  idcoffer int not null PRIMARY KEY ,
  nameco VARCHAR(30) default NULL,
  descco VARCHAR(30) default NULL,
  FOREIGN KEY(idcoffer) REFERENCES test99_poly_product (idprod)
);

CREATE TABLE test99_poly_offercomposition(
  offer int not null,
  product int not null, 
  CONSTRAINT unique_rel UNIQUE (offer, product)
);
