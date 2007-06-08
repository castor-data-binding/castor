DROP TABLE IF EXISTS test1158_extended;

CREATE TABLE test1158_extended (
  id            int NOT NULL default '0',
  description2  varchar(50) NOT NULL default '',
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO test1158_extended VALUES (1, 'This is the extended object.');

DROP TABLE IF EXISTS test1158_object;

CREATE TABLE test1158_object (
  id           int NOT NULL default '0',
  description  varchar(50) NOT NULL default '',
  saved        char(1) default '0',
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO test1158_object VALUES (1, 'This is the test object.', '');
