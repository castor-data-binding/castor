DROP TABLE test1355_country CASCADE CONSTRAINTS;

CREATE TABLE test1355_country (
    ID NUMERIC(18,0) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    ISO3_COUNTRY_CODE VARCHAR(3) ,
    RECORD_CREATED_BY_ID NUMERIC(18,0) ,
    RECORD_LAST_MOD_BY_ID NUMERIC(18,0) ,
    RECORD_CREATE_DATE DATE ,
    RECORD_LAST_MOD_DATE DATE
);

DROP TABLE test1355_state_prov CASCADE CONSTRAINTS;

CREATE TABLE test1355_state_prov (
    ID NUMERIC(18,0) NOT NULL,
    CODE VARCHAR(3) ,
    NAME VARCHAR(100) NOT NULL,
    COUNTRY_ID NUMERIC(18,0) NOT NULL,
    RECORD_CREATED_BY_ID NUMERIC(18,0) ,
    RECORD_LAST_MOD_BY_ID NUMERIC(18,0) ,
    RECORD_CREATE_DATE DATE ,
    RECORD_LAST_MOD_DATE DATE
);

DROP TABLE test1355_city CASCADE CONSTRAINTS;

CREATE TABLE test1355_city (
    ID NUMERIC(18,0) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    STATE_PROV_ID NUMERIC(18,0) NOT NULL,
    RECORD_CREATED_BY_ID NUMERIC(18,0) ,
    RECORD_LAST_MOD_BY_ID NUMERIC(18,0) ,
    RECORD_CREATE_DATE DATE ,
    RECORD_LAST_MOD_DATE DATE
);

DROP TABLE test1355_golf_course CASCADE CONSTRAINTS;

CREATE TABLE test1355_golf_course (
    ID NUMERIC(18,0) NOT NULL,
    COURSE_NAME VARCHAR(100) NOT NULL,
    HOLES INTEGER NOT NULL,
    CITY_ID NUMERIC(18,0) ,
    RECORD_CREATED_BY_ID NUMERIC(18,0) ,
    RECORD_LAST_MOD_BY_ID NUMERIC(18,0) ,
    RECORD_CREATE_DATE DATE ,
    RECORD_LAST_MOD_DATE DATE
);

DROP TABLE test1355_golf_course_tees CASCADE CONSTRAINTS;

CREATE TABLE test1355_golf_course_tees (
    ID NUMERIC(18,0) NOT NULL,
    COURSE_ID NUMERIC(18,0) NOT NULL,
    TEES_NAME VARCHAR(100) NOT NULL,
    TEES_COLOR VARCHAR(25) ,
    MENS_SLOPE INTEGER ,
    MENS_RATING DOUBLE PRECISION ,
    WOMENS_SLOPE INTEGER ,
    WOMENS_RATING DOUBLE PRECISION ,
    YARDAGE INTEGER ,
    RECORD_CREATED_BY_ID NUMERIC(18,0) ,
    RECORD_LAST_MOD_BY_ID NUMERIC(18,0) ,
    RECORD_CREATE_DATE DATE ,
    RECORD_LAST_MOD_DATE DATE
);

DROP TABLE test1355_golf_course_holes CASCADE CONSTRAINTS;

CREATE TABLE test1355_golf_course_holes (
    ID NUMERIC(18,0) NOT NULL,
    COURSE_TEES_ID NUMERIC(18,0) NOT NULL,
    NUM INTEGER NOT NULL,
    HOLE_NAME VARCHAR(100) ,
    PAR INTEGER ,
    YARDAGE INTEGER ,
    RECORD_CREATED_BY_ID NUMERIC(18,0) ,
    RECORD_LAST_MOD_BY_ID NUMERIC(18,0) ,
    RECORD_CREATE_DATE DATE ,
    RECORD_LAST_MOD_DATE DATE
);

insert into test1355_country (ID, NAME, ISO3_COUNTRY_CODE) values ('1', 'USA', 'USA');
insert into test1355_state_prov (ID, CODE, NAME, COUNTRY_ID) values ('1', 'WA', 'Washington', '1');
insert into test1355_city (ID, NAME, STATE_PROV_ID) values ('1', 'Seattle', '1');

insert into test1355_golf_course (ID, COURSE_NAME, HOLES, CITY_ID) values ('1', 'West Seattle', '18', '1');
insert into test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) values ('1', '1', 'White', 'white');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('1', '1', '1', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('2', '1', '2', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('3', '1', '3', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('4', '1', '4', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('5', '1', '5', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('6', '1', '6', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('7', '1', '7', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('8', '1', '8', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('9', '1', '9', '4');

insert into test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) values ('2', '1', 'Blue', 'blue');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('10', '2', '1', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('11', '2', '2', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('12', '2', '3', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('13', '2', '4', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('14', '2', '5', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('15', '2', '6', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('16', '2', '7', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('17', '2', '8', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('18', '2', '9', '4');

insert into test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) values ('3', '1', 'Red', 'red');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('19', '3', '1', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('20', '3', '2', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('21', '3', '3', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('22', '3', '4', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('23', '3', '5', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('24', '3', '6', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('25', '3', '7', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('26', '3', '8', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('27', '3', '9', '4');

insert into test1355_golf_course (ID, COURSE_NAME, HOLES, CITY_ID) values ('2', 'Jackson Park', '18', '1');
insert into test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) values ('4', '2', 'White', 'white');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('28', '4', '1', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('29', '4', '2', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('30', '4', '3', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('31', '4', '4', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('32', '4', '5', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('33', '4', '6', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('34', '4', '7', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('35', '4', '8', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('36', '4', '9', '4');

insert into test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) values ('5', '2', 'Blue', 'blue');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('37', '5', '1', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('38', '5', '2', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('39', '5', '3', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('40', '5', '4', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('41', '5', '5', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('42', '5', '6', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('43', '5', '7', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('44', '5', '8', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('45', '5', '9', '4');

insert into test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) values ('6', '2', 'Red', 'red');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('46', '6', '1', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('47', '6', '2', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('48', '6', '3', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('49', '6', '4', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('50', '6', '5', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('51', '6', '6', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('52', '6', '7', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('53', '6', '8', '4');
insert into test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) values ('54', '6', '9', '4');
