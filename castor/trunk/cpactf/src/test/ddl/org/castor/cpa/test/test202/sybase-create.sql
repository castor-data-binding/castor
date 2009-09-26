create table test202_fkf_entity_1 (
    id int NOT NULL,
    number int
)
go

create table test202_fkf_entity_n (
    id int NOT NULL,
    entity int NOT NULL,
    number int
)
go

insert into test202_fkf_entity_1 VALUES (1, 1)
go