create table test201_self_relation_parent (
  id          int                 not null,  
  name        varchar(255)        not null
)
/

create table test201_self_relation_extend (
  id          int                 not null,  
  parent_id   int              
)
/
