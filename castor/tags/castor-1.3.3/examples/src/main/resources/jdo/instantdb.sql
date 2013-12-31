; First load the JDBC driver and open a database.
d org.enhydra.instantdb.jdbc.idbDriver;
o jdbc:idb=test.prp;

; Record all results
e SET EXPORT "export0.txt" FIXEDLENGTH COLNAMEHEADER ROWNUMBERS CONTROLCOL SUMMARYHEADER;


; create castor JDO test tables and indexes

e DROP TABLE prod;
e CREATE TABLE prod (
  id        int PRIMARY KEY,
  name      varchar(200) not null,
  price     numeric(18,2) not null,
  group_id  int not null
);

e DROP TABLE prod_group;
e CREATE TABLE prod_group (
  id    int PRIMARY KEY,
  name  varchar(200) not null
);

e DROP TABLE prod_detail;
e CREATE TABLE prod_detail (
  id      int PRIMARY KEY,
  prod_id int not null,
  name    varchar(200) not null
);

e DROP TABLE computer;
e CREATE TABLE computer (
  id   int PRIMARY KEY,
  cpu  varchar(200) not null
);


e DROP TABLE category;
e CREATE TABLE category (
  id   int PRIMARY KEY,
  name varchar(200) not null
);

e DROP TABLE category_prod;
e CREATE TABLE category_prod (
  prod_id   int PRIMARY KEY,
  category_id   int not null
);





c close;
 
