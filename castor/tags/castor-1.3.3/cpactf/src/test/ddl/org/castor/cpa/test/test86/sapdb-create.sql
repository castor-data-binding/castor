create sequence test86_circ_brother_seq
//
create table test86_circ_brother (
    brother_id integer not null,
    brother_sibling integer,
    primary key (brother_id)
)
//
create sequence test86_circ_sister_seq
//
create table test86_circ_sister (
    sister_id integer not null,
    sister_sibling integer,
    primary key (sister_id)
)
//
