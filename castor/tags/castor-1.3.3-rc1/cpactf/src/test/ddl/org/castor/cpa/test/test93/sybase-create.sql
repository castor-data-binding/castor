create table test93_foo (
  id                int not null,
  field             varchar(200) not null
)
go

insert into test93_foo (id, field) values (1, 'a foo')
go
insert into test93_foo (id, field) values (2, 'a bar')
go

create table test93_bar (
  id        int not null
)
go

insert into test93_bar (id) values (2)
go

create table test93_customer (
    id                int not null,
    description     varchar(200) not null            
)
go

insert into test93_customer (id, description) values (1, 'alice')
go
insert into test93_customer (id, description) values (2, 'bob')
go

create table test93_subscription (
    id                int not null,
    createddate        timestamp,
    description        varchar(200),
    customer_id        int not null
)
go

insert into test93_subscription (id, createddate, customer_id) values (1, CURRENT_TIMESTAMP, 2)
go
insert into test93_subscription (id, createddate, customer_id) values (2, CURRENT_TIMESTAMP, 2)
go