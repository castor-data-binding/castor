CREATE TABLE test71_container (
  id int NOT NULL ,
  name varchar(200) default NULL,
  PRIMARY KEY (id)
)
go

INSERT INTO test71_container (id, name) VALUES 
  (1,'Container 1'),
  (2,'Container 2'),
  (3,'Container 3'),
  (4,'Container 4')
go

CREATE TABLE test71_container_item (
  id int NOT NULL,
  item int DEFAULT NULL,
  value varchar(200) DEFAULT NULL,
  PRIMARY KEY (id)
)
go

INSERT INTO test71_container_item (id, item, value) VALUES 
  (1,1,'Container item 1'),
  (2,2,'Container item 2'),
  (3,3,'Container item 3'),
  (4,4,'Container item 4'),
  (5,1,'Container item 5'),
  (6,2,'Container item 6'),
  (7,3,'Container item 7'),
  (8,4,'Container item 8')
go
