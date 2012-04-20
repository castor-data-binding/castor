create table test12_conv (
    id                 int         not null,
    bool_int           int,
    bool_int_minus     int,
    bool_bigdec        numeric,
    bool_bigdec_minus  numeric,
    byte_int           int         ,
    short_int          int         ,
    long_int           int         ,
    double_int         int         ,
    float_int          float       ,
    int_bigdec         numeric     ,
    float_bigdec       numeric     ,
    double_bigdec      numeric     ,
    int_string         varchar(20) ,
    long_string        varchar(20) ,
    bigdec_string      varchar(20) ,
    float_string       varchar(20) ,
    double_string      varchar(20) 
);

create unique index test12_conv_pk on test12_conv( id );
