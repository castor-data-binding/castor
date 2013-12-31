CREATE TABLE test2858_financetitle ( 
    oid VARCHAR(8) NOT NULL, 
    dt TIMESTAMP, 
    vl DECIMAL(12,2), 
    PRIMARY KEY (oid)
);

CREATE TABLE test2858_billcollection (
	oid VARCHAR(8) NOT NULL,
    billcollectiontype VARCHAR(20), 
    PRIMARY KEY (oid)
);

ALTER TABLE test2858_billcollection
ADD FOREIGN KEY (oid)
REFERENCES test2858_financetitle (oid);

CREATE TABLE test2858_tradenote ( 
	oid VARCHAR(8) NOT NULL,
    notenumber INTEGER, 
    PRIMARY KEY (oid)
);

ALTER TABLE test2858_tradenote
ADD FOREIGN KEY (oid)
REFERENCES test2858_billcollection (oid);

CREATE TABLE test2858_paymenttitle ( 
    oid VARCHAR(8) NOT NULL,
    paymenttype VARCHAR(20), 
    PRIMARY KEY (OID)
);

ALTER TABLE test2858_paymenttitle
ADD FOREIGN KEY (oid)
REFERENCES test2858_financetitle (oid);

CREATE TABLE test2858_money ( 
	oid VARCHAR(8) NOT NULL,
    banknotevalue DECIMAL(12,2), 
    quantity INTEGER, 
    PRIMARY KEY (OID)
);

ALTER TABLE test2858_money
ADD FOREIGN KEY (oid)
REFERENCES test2858_paymenttitle (oid);

CREATE TABLE test2858_payment ( 
    oid VARCHAR(8) NOT NULL, 
    myfinancetitle VARCHAR(8), 
    myfinancetitlepaid VARCHAR(8), 
    PRIMARY KEY (oid)
);

ALTER TABLE test2858_payment
ADD FOREIGN KEY (myfinancetitle)
REFERENCES test2858_financetitle (oid);

ALTER TABLE test2858_payment
ADD FOREIGN KEY (myfinancetitlepaid)
REFERENCES test2858_financetitle (oid);

CREATE TABLE test2858_financetitlerelation ( 
    oid VARCHAR(8) NOT NULL,
    objecttype VARCHAR(20),
    myobject VARCHAR(8),
    myfinancetitle VARCHAR(8), 
    PRIMARY KEY (oid)
);

ALTER TABLE test2858_financetitlerelation
ADD FOREIGN KEY (myfinancetitle)
REFERENCES test2858_financetitle (oid);

CREATE TABLE test2858_invoiceparcel ( 
    oid VARCHAR(8) NOT NULL,
    parcelnumber INTEGER,
    vl DECIMAL(12,2),
    PRIMARY KEY (oid)
);
