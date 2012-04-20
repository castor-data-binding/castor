create table test86_circ_brother (
    brother_id int not null,
    brother_sibling int,
    constraint test86_pk_brother primary key (brother_id))
go

create table test86_circ_sister (
    sister_id int not null,
    sister_sibling int,
    constraint test86_pk_sister primary key (sister_id))
go
