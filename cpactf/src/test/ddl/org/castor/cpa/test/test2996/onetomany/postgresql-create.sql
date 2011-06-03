create table test2996_onetomany_house(
    id INTEGER PRIMARY KEY NOT NULL
);

create table test2996_onetomany_flat(
    id       INTEGER PRIMARY KEY NOT NULL,
    house_id INTEGER NOT NULL
    CONSTRAINT fk_test2996_onetomany_fh
      REFERENCES test2996_onetomany_house(id)
);

