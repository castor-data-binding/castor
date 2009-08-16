create table test200_self_relation_folder (
  id          int             not null,  
  name        varchar(255)    not null,
  parent_id   int             default null
);
