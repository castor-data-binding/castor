create table test70_col (
  id integer not null
)
go

create unique index test70_col_pk on test70_col( id )
go

create table test70_item (
  iid integer not null,
  id  integer           
)
go

create unique index test70_item_pk on test70_item( iid )
go

create table test70_comp_item (
  iid integer not null,
  id integer
)
go

create unique index test70_comp_item_pk on test70_comp_item( iid )
go
