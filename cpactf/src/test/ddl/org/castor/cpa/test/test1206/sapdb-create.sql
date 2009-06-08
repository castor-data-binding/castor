CREATE TABLE test1206_country (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    PRIMARY KEY (oid)
)
//

CREATE TABLE test1206_state (
    oid VARCHAR(8) NOT NULL,
    name VARCHAR(60) NOT NULL,
    country VARCHAR(8) NOT NULL,
    PRIMARY KEY (oid),
    FOREIGN KEY (country) REFERENCES test1206_country (oid)
)
//
