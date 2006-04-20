-- The test stored procedure on TransactSQL
drop procedure sp_check_permissions
go
create procedure sp_check_permissions @userName varchar(200),
                                      @groupName varchar(200) AS
    SELECT id, value1, value2 FROM test_table WHERE value1 = @userName
    SELECT id, value1, value2 FROM test_table WHERE value2 = @groupName

