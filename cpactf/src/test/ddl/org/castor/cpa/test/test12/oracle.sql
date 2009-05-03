drop table test12_conv;

create table test12_conv (
    id                 int         not null,
    bool_int           int         null,
    bool_int_minus     int         null,
    bool_bigdec        numeric     null,
    bool_bigdec_minus  numeric     null,
    byte_int           int         null,
    short_int          int         null,
    long_int           int         null,
    double_int         int         null,
    float_int          float       null,
    int_bigdec         numeric     null,
    float_bigdec       numeric     null,
    double_bigdec      numeric     null,
    int_string         varchar(20) null,
    long_string        varchar(20) null,
    bigdec_string      varchar(20) null,
    float_string       varchar(20) null,
    double_string      varchar(20) null
);

create unique index test12_conv_pk on test12_conv( id );
