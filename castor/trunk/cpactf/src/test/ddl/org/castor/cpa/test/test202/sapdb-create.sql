create table test202_foreign_key_first_entity_1 (
    id int NOT NULL,
    number int
)
//

create table test202_foreign_key_first_entity_n (
    id int NOT NULL,
    entity int NOT NULL,
    number int
)
//

insert into test202_foreign_key_first_entity_1 VALUES (1, 1)
//