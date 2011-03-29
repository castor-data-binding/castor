create table test202_fkf_entity_1 (
    id int NOT NULL,
    numb int
);

create table test202_fkf_entity_n (
    id int NOT NULL,
    entity int NOT NULL,
    numb int
);

insert into test202_fkf_entity_1 VALUES (1, 1);