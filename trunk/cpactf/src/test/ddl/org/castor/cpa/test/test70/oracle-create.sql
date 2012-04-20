create table test70_col (
  id integer not null
)
/

create unique index test70_col_pk on test70_col( id )
/

create table test70_item (
  iid integer not null,
  id  integer null         
)
/

create unique index test70_item_pk on test70_item( iid )
/

create table test70_comp_item (
  iid integer not null,
  id integer
)
/

create unique index test70_comp_item_pk on test70_comp_item( iid )
/
