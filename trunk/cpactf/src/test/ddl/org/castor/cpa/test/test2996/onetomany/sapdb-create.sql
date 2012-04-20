create table test2996_onetomany_house(
    id INTEGER PRIMARY KEY NOT NULL
)
//
create table test2996_onetomany_flat(
    id       INTEGER PRIMARY KEY NOT NULL,
    house_id INTEGER NOT NULL,
    FOREIGN KEY test2996_onetomany_flat_house (house_id)
      REFERENCES test2996_onetomany_house(id)
)
//