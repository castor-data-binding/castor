CREATE TABLE test1355_state_prov(
	id bigint NOT NULL,
	code varchar(3),
	name varchar(100) NOT NULL,
	country_id bigint NOT NULL,
	record_created_by_id bigint,
	record_last_mod_by_id bigint,
	record_create_date datetime,
	record_last_mod_date datetime
) 
GO

CREATE TABLE test1355_golf_course_tees(
	id bigint NOT NULL,
	course_id bigint NOT NULL,
	tees_name varchar(100) NOT NULL,
	tees_color varchar(25),
	mens_slope int,
	mens_rating double precision,
	womens_slope int,
	womens_rating double precision,
	yardage int,
	record_created_by_id bigint,
	record_last_mod_by_id bigint,
	record_create_date datetime,
	record_last_mod_date datetime
)
GO

CREATE TABLE test1355_golf_course_holes(
	id bigint NOT NULL,
	course_tees_id bigint NOT NULL,
	num int NOT NULL,
	hole_name varchar(100),
	par int,
	yardage int,
	record_created_by_id bigint,
	record_last_mod_by_id bigint,
	record_create_date datetime,
	record_last_mod_date datetime
)
GO

CREATE TABLE test1355_golf_course(
	id bigint NOT NULL,
	course_name varchar(100) NOT NULL,
	holes int NOT NULL,
	city_id bigint,
	record_created_by_id bigint,
	record_last_mod_by_id bigint,
	record_create_date datetime,
	record_last_mod_date datetime
)
GO

CREATE TABLE test1355_country(
	id bigint NOT NULL,
	name varchar(100) NOT NULL,
	iso3_country_code varchar(3),
	record_created_by_id bigint,
	record_last_mod_by_id bigint,
	record_create_date datetime,
	record_last_mod_date datetime
)
GO

CREATE TABLE test1355_city(
	id bigint NOT NULL,
	name varchar(100) NOT NULL,
	state_prov_id bigint NOT NULL,
	record_created_by_id bigint,
	record_last_mod_by_id bigint,
	record_create_date datetime,
	record_last_mod_date datetime
)
GO

INSERT INTO test1355_country (id, name, iso3_country_code) VALUES ('1', 'USA', 'USA')
GO
INSERT INTO test1355_state_prov (id, code, name, country_id) VALUES ('1', 'WA', 'Washington', '1')
GO
INSERT INTO test1355_city (id, name, state_prov_id) VALUES ('1', 'Seattle', '1')
GO

INSERT INTO test1355_golf_course (id, course_name, holes, city_id) VALUES ('1', 'West Seattle', '18', '1')
GO
INSERT INTO test1355_golf_course_tees (id, course_id, tees_name, tees_color) VALUES ('1', '1', 'White', 'white')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('1', '1', '1', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('2', '1', '2', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('3', '1', '3', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('4', '1', '4', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('5', '1', '5', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('6', '1', '6', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('7', '1', '7', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('8', '1', '8', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('9', '1', '9', '4')
GO

INSERT INTO test1355_golf_course_tees (id, course_id, tees_name, tees_color) VALUES ('2', '1', 'Blue', 'blue')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('10', '2', '1', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('11', '2', '2', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('12', '2', '3', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('13', '2', '4', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('14', '2', '5', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('15', '2', '6', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('16', '2', '7', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('17', '2', '8', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('18', '2', '9', '4')
GO

INSERT INTO test1355_golf_course_tees (id, course_id, tees_name, tees_color) VALUES ('3', '1', 'Red', 'red')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('19', '3', '1', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('20', '3', '2', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('21', '3', '3', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('22', '3', '4', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('23', '3', '5', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('24', '3', '6', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('25', '3', '7', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('26', '3', '8', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('27', '3', '9', '4')
GO

INSERT INTO test1355_golf_course (id, course_name, holes, city_id) VALUES ('2', 'Jackson Park', '18', '1')
GO
INSERT INTO test1355_golf_course_tees (id, course_id, tees_name, tees_color) VALUES ('4', '2', 'White', 'white')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('28', '4', '1', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('29', '4', '2', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('30', '4', '3', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('31', '4', '4', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('32', '4', '5', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('33', '4', '6', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('34', '4', '7', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('35', '4', '8', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('36', '4', '9', '4')
GO

INSERT INTO test1355_golf_course_tees (id, course_id, tees_name, tees_color) VALUES ('5', '2', 'Blue', 'blue')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('37', '5', '1', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('38', '5', '2', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('39', '5', '3', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('40', '5', '4', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('41', '5', '5', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('42', '5', '6', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('43', '5', '7', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('44', '5', '8', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('45', '5', '9', '4')
GO

INSERT INTO test1355_golf_course_tees (id, course_id, tees_name, tees_color) VALUES ('6', '2', 'Red', 'red')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('46', '6', '1', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('47', '6', '2', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('48', '6', '3', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('49', '6', '4', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('50', '6', '5', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('51', '6', '6', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('52', '6', '7', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('53', '6', '8', '4')
GO
INSERT INTO test1355_golf_course_holes (id, course_tees_id, num, par) VALUES ('54', '6', '9', '4')
GO
