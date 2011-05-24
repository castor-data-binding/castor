CREATE TABLE test2861_person ( 
    oid VARCHAR(8) NOT NULL, 
    name VARCHAR(32),
    creator VARCHAR(8),
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_person
ADD FOREIGN KEY (creator)
REFERENCES test2861_person (oid);

CREATE TABLE test2861_naturalperson ( 
    oid VARCHAR(8) NOT NULL, 
    socialsecuritynumber VARCHAR(16), 
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_naturalperson
ADD FOREIGN KEY (oid)
REFERENCES test2861_person (oid);

CREATE TABLE test2861_legalperson ( 
	oid VARCHAR(8) NOT NULL, 
    federaltaxnumber VARCHAR(16),
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_legalperson
ADD FOREIGN KEY (oid)
REFERENCES test2861_person (oid);

CREATE TABLE test2861_product ( 
	oid VARCHAR(8) NOT NULL,
    eancode VARCHAR(13),
    PRIMARY KEY (OID)
);

CREATE TABLE test2861_motorcycle ( 
    oid VARCHAR(8) NOT NULL, 
    chassisnumber VARCHAR(16), 
    holder VARCHAR(8) NOT NULL, 
    reseller VARCHAR(8) NOT NULL, 
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_motorcycle
ADD FOREIGN KEY (oid)
REFERENCES test2861_product (oid);

ALTER TABLE test2861_motorcycle
ADD FOREIGN KEY (holder)
REFERENCES test2861_person (oid);

ALTER TABLE test2861_motorcycle
ADD FOREIGN KEY (reseller)
REFERENCES test2861_person (oid);

CREATE TABLE test2861_invoice ( 
    oid VARCHAR(8),
    nb INTEGER,
    emitter VARCHAR(8), 
    billto VARCHAR(8), 
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_invoice
ADD FOREIGN KEY (emitter)
REFERENCES test2861_person (oid);

ALTER TABLE test2861_invoice
ADD FOREIGN KEY (billTo)
REFERENCES test2861_person (oid);

CREATE TABLE test2861_invoiceitem ( 
    oid VARCHAR(8) NOT NULL, 
    invoice VARCHAR(8), 
    product VARCHAR(8), 
    quantity INTEGER, 
    price DECIMAL(12,2), 
    total DECIMAL(12,2), 
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_invoiceitem
ADD FOREIGN KEY (invoice)
REFERENCES test2861_invoice (oid);

ALTER TABLE test2861_invoiceitem
ADD FOREIGN KEY (product)
REFERENCES test2861_product (oid);

CREATE TABLE test2861_parameter ( 
    oid VARCHAR(8) NOT NULL, 
    person VARCHAR(8),
    idsys VARCHAR(6),
    intvalue INTEGER, 
    PRIMARY KEY (OID)
);

ALTER TABLE test2861_parameter
ADD FOREIGN KEY (person)
REFERENCES test2861_person (oid);