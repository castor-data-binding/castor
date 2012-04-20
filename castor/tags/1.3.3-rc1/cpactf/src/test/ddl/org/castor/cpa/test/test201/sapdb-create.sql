create table test201_self_rel_parent (
  id          int                 not null,  
  name        varchar(255)        not null
)
//

create table test201_self_rel_extend (
  id          int                 not null,  
  parent_id   int              
)
//
