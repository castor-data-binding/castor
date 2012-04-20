CREATE TABLE test99_poly_Product(
  IdProd int not null PRIMARY KEY,
  NameProd   VARCHAR(30) default NULL,
  DescProd   VARCHAR(30) default NULL
)
//

CREATE TABLE test99_poly_ActProduct(
  IdAct int not null PRIMARY KEY REFERENCES test99_poly_Product (IdProd),
  BestSeason VARCHAR(30) default NULL
)
//

CREATE TABLE test99_poly_ComposedOffer(
  IdCOffer int not null PRIMARY KEY REFERENCES test99_poly_Product (IdProd),
  NameCO   VARCHAR(30) default NULL,
  DescCO   VARCHAR(30) default NULL
)
//

CREATE TABLE test99_poly_OfferComposition(
  Offer NUMERIC(10) not null,
  Product NUMERIC(10) not null, 
  CONSTRAINT unique_rel UNIQUE (Offer, Product)
)
//
