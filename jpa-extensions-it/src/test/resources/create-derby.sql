CREATE SCHEMA TEST;

CREATE TABLE broadcast(
    id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    actor_id INTEGER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE actor(
    id INTEGER NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE broadcast ADD CONSTRAINT fk_actor_id
	FOREIGN KEY (actor_id) REFERENCES actor (id);
	


//CREATE TABLE show(
//   id INTEGER NOT NULL,
//    name VARCHAR(200) NOT NULL,
//    PRIMARY KEY (id),
//    FOREIGN KEY (timeslot_id)
//        REFERENCES timeslot(id)
//);

//CREATE TABLE timeslot(
//    id INTEGER NOT NULL,
//    name VARCHAR(200) NOT NULL,
//    PRIMARY KEY(id),
//    FOREIGN KEY (show_id)
//    	REFERENCES show(id)
//);

CREATE TABLE staff(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    pay NUMERIC(7,2) NOT NULL,
    PRIMARY KEY(id)
);
