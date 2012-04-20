CREATE TABLE test71_container (
  id int NOT NULL ,
  name varchar(200) default NULL,
  PRIMARY KEY (id)
);

INSERT INTO test71_container (id, name) VALUES (1,'Container 1');
 INSERT INTO test71_container (id, name) VALUES  (2,'Container 2');
 INSERT INTO test71_container (id, name) VALUES  (3,'Container 3');
 INSERT INTO test71_container (id, name) VALUES  (4,'Container 4');

CREATE TABLE test71_container_item (
  id int NOT NULL,
  item int DEFAULT NULL,
  ivalue varchar(200) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO test71_container_item (id, item, ivalue) VALUES (1,1,'Container item 1');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (2,2,'Container item 2');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (3,3,'Container item 3');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (4,4,'Container item 4');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (5,1,'Container item 5');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (6,2,'Container item 6');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (7,3,'Container item 7');
INSERT INTO test71_container_item (id, item, ivalue) VALUES (8,4,'Container item 8');
	
