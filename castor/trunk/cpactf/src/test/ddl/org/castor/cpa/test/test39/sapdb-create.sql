create table test39_entity (
  id      integer       not null,
  value1  varchar(200)  not null,
  value2  varchar(200),
  primary key (id)
)
//
create dbproc proc_check_permissions (IN userName VARCHAR(200), IN groupName VARCHAR(200))
returns cursor as $CURSOR = 'CHECK_PERMISSIONS_CURSOR';
DECLARE :$CURSOR CURSOR FOR
SELECT id, value1, value2 FROM test.test39_entity WHERE value1 = :userName
           UNION SELECT id, value1, value2 FROM test.test39_entity WHERE value2 = :groupName;
//
