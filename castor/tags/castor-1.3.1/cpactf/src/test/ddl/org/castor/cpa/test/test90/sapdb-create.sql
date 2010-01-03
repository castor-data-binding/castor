create table test90_lazy_11_chd (
  id        int not null,
  descr     varchar(20) not null
)
//

create table test90_lazy_11_par (
  id        int not null,
  descr     varchar(20) not null,
  child_id  int
)
//

create table test90_lazy_11_author (
  id            int not null,
  first_name        varchar(100) not null,
  last_name        varchar(100) not null
)
//

create table test90_lazy_11_book (
  id            int not null,
  name             varchar(100) not null,
  author_id        int not null
)
//

insert into test90_lazy_11_chd (id, descr) values (1, 'child 1')
//
insert into test90_lazy_11_chd (id, descr) values (2, 'child 2')
//
insert into test90_lazy_11_chd (id, descr) values (3, 'child 3')
//
insert into test90_lazy_11_chd (id, descr) values (4, 'child 4')
//

insert into test90_lazy_11_par (id, descr, child_id) values (1, 'parent 1', 1)
//
insert into test90_lazy_11_par (id, descr, child_id) values (2, 'parent 2', 2)
//
insert into test90_lazy_11_par (id, descr, child_id) values (3, 'parent 3', 1)
//
insert into test90_lazy_11_par (id, descr, child_id) values (5, 'parent 5', null)
//

insert into test90_lazy_11_author (id, first_name, last_name) values (1, 'Joe', 'Writer')
//
 
insert into test90_lazy_11_book (id, name, author_id) select 1, 'test book', test90_lazy_11_author.id from test90_lazy_11_author
//
