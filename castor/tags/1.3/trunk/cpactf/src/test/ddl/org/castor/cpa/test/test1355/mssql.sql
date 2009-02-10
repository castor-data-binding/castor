DROP TABLE test1355_city
GO

DROP TABLE test1355_country
GO

DROP TABLE test1355_golf_course
GO

DROP TABLE test1355_golf_course_holes
GO

DROP TABLE test1355_golf_course_tees
GO

DROP TABLE test1355_state_prov
GO

CREATE TABLE test1355_state_prov(
	ID bigint NOT NULL,
	CODE varchar(3),
	NAME varchar(100) NOT NULL,
	COUNTRY_ID bigint NOT NULL,
	RECORD_CREATED_BY_ID bigint,
	RECORD_LAST_MOD_BY_ID bigint,
	RECORD_CREATE_DATE datetime,
	RECORD_LAST_MOD_DATE datetime
) 
GO

CREATE TABLE test1355_golf_course_tees(
	ID bigint NOT NULL,
	COURSE_ID bigint NOT NULL,
	TEES_NAME varchar(100) NOT NULL,
	TEES_COLOR varchar(25),
	MENS_SLOPE int,
	MENS_RATING double precision,
	WOMENS_SLOPE int,
	WOMENS_RATING double precision,
	YARDAGE int,
	RECORD_CREATED_BY_ID bigint,
	RECORD_LAST_MOD_BY_ID bigint,
	RECORD_CREATE_DATE datetime,
	RECORD_LAST_MOD_DATE datetime
)
GO

CREATE TABLE test1355_golf_course_holes(
	ID bigint NOT NULL,
	COURSE_TEES_ID bigint NOT NULL,
	NUM int NOT NULL,
	HOLE_NAME varchar(100),
	PAR int,
	YARDAGE int,
	RECORD_CREATED_BY_ID bigint,
	RECORD_LAST_MOD_BY_ID bigint,
	RECORD_CREATE_DATE datetime,
	RECORD_LAST_MOD_DATE datetime
)
GO

CREATE TABLE test1355_golf_course(
	ID bigint NOT NULL,
	COURSE_NAME varchar(100) NOT NULL,
	HOLES int NOT NULL,
	CITY_ID bigint,
	RECORD_CREATED_BY_ID bigint,
	RECORD_LAST_MOD_BY_ID bigint,
	RECORD_CREATE_DATE datetime,
	RECORD_LAST_MOD_DATE datetime
)
GO

CREATE TABLE test1355_country(
	ID bigint NOT NULL,
	NAME varchar(100) NOT NULL,
	ISO3_COUNTRY_CODE varchar(3),
	RECORD_CREATED_BY_ID bigint,
	RECORD_LAST_MOD_BY_ID bigint,
	RECORD_CREATE_DATE datetime,
	RECORD_LAST_MOD_DATE datetime
)
GO

CREATE TABLE test1355_city(
	ID bigint NOT NULL,
	NAME varchar(100) NOT NULL,
	STATE_PROV_ID bigint NOT NULL,
	RECORD_CREATED_BY_ID bigint,
	RECORD_LAST_MOD_BY_ID bigint,
	RECORD_CREATE_DATE datetime,
	RECORD_LAST_MOD_DATE datetime
)
GO

INSERT INTO test1355_country (ID, NAME, ISO3_COUNTRY_CODE) VALUES ('1', 'USA', 'USA');
INSERT INTO test1355_state_prov (ID, CODE, NAME, COUNTRY_ID) VALUES ('1', 'WA', 'Washington', '1');
INSERT INTO test1355_city (ID, NAME, STATE_PROV_ID) VALUES ('1', 'Seattle', '1');

INSERT INTO test1355_golf_course (ID, COURSE_NAME, HOLES, CITY_ID) VALUES ('1', 'West Seattle', '18', '1');
INSERT INTO test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) VALUES ('1', '1', 'White', 'white');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('1', '1', '1', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('2', '1', '2', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('3', '1', '3', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('4', '1', '4', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('5', '1', '5', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('6', '1', '6', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('7', '1', '7', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('8', '1', '8', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('9', '1', '9', '4');

INSERT INTO test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) VALUES ('2', '1', 'Blue', 'blue');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('10', '2', '1', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('11', '2', '2', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('12', '2', '3', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('13', '2', '4', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('14', '2', '5', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('15', '2', '6', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('16', '2', '7', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('17', '2', '8', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('18', '2', '9', '4');

INSERT INTO test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) VALUES ('3', '1', 'Red', 'red');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('19', '3', '1', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('20', '3', '2', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('21', '3', '3', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('22', '3', '4', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('23', '3', '5', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('24', '3', '6', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('25', '3', '7', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('26', '3', '8', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('27', '3', '9', '4');

INSERT INTO test1355_golf_course (ID, COURSE_NAME, HOLES, CITY_ID) VALUES ('2', 'Jackson Park', '18', '1');
INSERT INTO test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) VALUES ('4', '2', 'White', 'white');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('28', '4', '1', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('29', '4', '2', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('30', '4', '3', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('31', '4', '4', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('32', '4', '5', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('33', '4', '6', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('34', '4', '7', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('35', '4', '8', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('36', '4', '9', '4');

INSERT INTO test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) VALUES ('5', '2', 'Blue', 'blue');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('37', '5', '1', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('38', '5', '2', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('39', '5', '3', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('40', '5', '4', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('41', '5', '5', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('42', '5', '6', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('43', '5', '7', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('44', '5', '8', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('45', '5', '9', '4');

INSERT INTO test1355_golf_course_tees (ID, COURSE_ID, TEES_NAME, TEES_COLOR) VALUES ('6', '2', 'Red', 'red');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('46', '6', '1', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('47', '6', '2', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('48', '6', '3', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('49', '6', '4', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('50', '6', '5', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('51', '6', '6', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('52', '6', '7', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('53', '6', '8', '4');
INSERT INTO test1355_golf_course_holes (ID, COURSE_TEES_ID, NUM, PAR) VALUES ('54', '6', '9', '4');