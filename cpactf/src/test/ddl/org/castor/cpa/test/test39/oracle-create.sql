create table test39_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
/
create unique index test39_entity_pk on test39_entity ( id )
/

CREATE OR REPLACE PACKAGE test AS
    TYPE TestCursor IS REF CURSOR RETURN test39_entity%ROWTYPE;
END test;
/
CREATE OR REPLACE FUNCTION proc_check_permissions (userName VARCHAR, groupName VARCHAR)
RETURN test.TestCursor AS res test.TestCursor;
BEGIN
    OPEN res FOR SELECT id, value1, value2 FROM test39_entity WHERE value1 = userName
           UNION SELECT id, value1, value2 FROM test39_entity WHERE value2 = groupName;
    RETURN res;
END;
/

