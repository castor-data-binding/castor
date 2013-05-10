create table test39_entity (
  id      int          not null,
  value1  varchar(200) not null,
  value2  varchar(200) null
)
go
create unique index test39_entity_pk on test39_entity ( id )
go

create procedure proc_check_permissions @userName varchar(200),
                                        @groupName varchar(200) AS
    SELECT id, value1, value2 FROM test39_entity WHERE value1 = @userName
    SELECT id, value1, value2 FROM test39_entity WHERE value2 = @groupName
go
sp_procxmode proc_check_permissions, "anymode"
go
