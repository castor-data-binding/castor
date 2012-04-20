create table test2996_onetomany_house(
    id INTEGER NOT NULL,
    PRIMARY KEY(id)
);

create table test2996_onetomany_flat(
    id INTEGER NOT NULL,
    house_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (house_id)
        REFERENCES test2996_onetomany_house(id)
);