CREATE TABLE IF NOT EXISTS test81_test_depends_ns (
  id int(11) NOT NULL auto_increment,
  master_id int(11) NOT NULL default '0',
  descrip varchar(50) NOT NULL default '',
  PRIMARY KEY  (id),
  KEY master_id (master_id)
);

CREATE TABLE IF NOT EXISTS test81_test_master_ns (
  id int(11) NOT NULL auto_increment,
  descrip varchar(50) NOT NULL default '',
  PRIMARY KEY  (id)
);