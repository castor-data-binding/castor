create table test2996_onetoone_address(
    id INTEGER NOT NULL,
    PRIMARY KEY (id)
);

create table test2996_onetoone_employee(
    id INTEGER NOT NULL,
    address_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id)
        REFERENCES test2996_onetoone_address(id)
);
