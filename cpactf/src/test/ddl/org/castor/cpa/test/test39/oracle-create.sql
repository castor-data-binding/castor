create table test39_entity (
  id      int           not null,
  value1  varchar(200)  not null,
  value2  varchar(200)
)
/
create unique index test39_entity_pk on test39_entity ( id )
/

create or replace package types as
    type cursorType is ref cursor;
end types;
/

create or replace function proc_check_permissions (userName VARCHAR, groupName VARCHAR)
return types.cursortype as cursorResult types.cursorType;
begin
    open cursorResult for
           SELECT id, value1, value2 FROM test39_entity WHERE value1 = userName
           UNION SELECT id, value1, value2 FROM test39_entity WHERE value2 = groupName;
    return cursorResult;
end proc_check_permissions;
/
