CREATE TABLE test1355_country (
	id BIGINT NOT NULL,
	name VARCHAR(100) NOT NULL,
	iso3_country_code VARCHAR(3) ,
	record_created_by_id BIGINT ,
	record_last_mod_by_id BIGINT ,
	record_create_date TIMESTAMP ,
	record_last_mod_date TIMESTAMP
);

CREATE TABLE test1355_state_prov (
	id BIGINT NOT NULL,
	code VARCHAR(3) ,
	name VARCHAR(100) NOT NULL,
	country_id BIGINT NOT NULL,
	record_created_by_id BIGINT ,
	record_last_mod_by_id BIGINT ,
	record_create_date TIMESTAMP ,
	record_last_mod_date TIMESTAMP
);

CREATE TABLE test1355_city (
	id BIGINT NOT NULL,
	name VARCHAR(100) NOT NULL,
	state_prov_id BIGINT NOT NULL,
	record_created_by_id BIGINT ,
	record_last_mod_by_id BIGINT ,
	record_create_date TIMESTAMP ,
	record_last_mod_date TIMESTAMP
);

CREATE TABLE test1355_golf_course (
	id BIGINT NOT NULL,
	course_name VARCHAR(100) NOT NULL,
	holes INTEGER NOT NULL,
	city_id BIGINT ,
	record_created_by_id BIGINT ,
	record_last_mod_by_id BIGINT ,
	record_create_date TIMESTAMP ,
	record_last_mod_date TIMESTAMP
);

CREATE TABLE test1355_golf_course_tees (
	id BIGINT NOT NULL,
	course_id BIGINT NOT NULL,
	tees_name VARCHAR(100) NOT NULL,
	tees_color VARCHAR(25) ,
	mens_slope INTEGER ,
	mens_rating DOUBLE ,
	womens_slope INTEGER ,
	womens_rating DOUBLE ,
	yardage INTEGER ,
	record_created_by_id BIGINT ,
	record_last_mod_by_id BIGINT ,
	record_create_date TIMESTAMP ,
	record_last_mod_date TIMESTAMP
);

CREATE TABLE test1355_golf_course_holes (
	id BIGINT NOT NULL,
	course_tees_id BIGINT NOT NULL,
	num INTEGER NOT NULL,
	hole_name VARCHAR(100) ,
	par INTEGER ,
	yardage INTEGER ,
	record_created_by_id BIGINT ,
	record_last_mod_by_id BIGINT ,
	record_create_date TIMESTAMP ,
	record_last_mod_date TIMESTAMP
);

insert into test1355_country (id, name, iso3_country_code) values (1, 'USA', 'USA');
insert into test1355_state_prov (id, code, name, country_id) values (1, 'WA', 'Washington', 1);
insert into test1355_city (id, name, state_prov_id) values (1, 'Seattle', 1);

insert into test1355_golf_course (id, course_name, holes, city_id) values (1, 'West Seattle', 18, 1);
insert into test1355_golf_course_tees (id, course_id, tees_name, tees_color) values (1, 1, 'White', 'white');
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (1, 1, 1, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (2, 1, 2, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (3, 1, 3, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (4, 1, 4, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (5, 1, 5, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (6, 1, 6, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (7, 1, 7, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (8, 1, 8, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (9, 1, 9, 4);

insert into test1355_golf_course_tees (id, course_id, tees_name, tees_color) values (2, 1, 'Blue', 'blue');
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (10, 2, 1, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (11, 2, 2, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (12, 2, 3, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (13, 2, 4, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (14, 2, 5, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (15, 2, 6, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (16, 2, 7, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (17, 2, 8, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (18, 2, 9, 4);

insert into test1355_golf_course_tees (id, course_id, tees_name, tees_color) values (3, 1, 'Red', 'red');
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (19, 3, 1, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (20, 3, 2, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (21, 3, 3, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (22, 3, 4, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (23, 3, 5, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (24, 3, 6, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (25, 3, 7, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (26, 3, 8, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (27, 3, 9, 4);

insert into test1355_golf_course (id, course_name, holes, city_id) values (2, 'Jackson Park', 18, 1);
insert into test1355_golf_course_tees (id, course_id, tees_name, tees_color) values (4, 2, 'White', 'white');
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (28, 4, 1, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (29, 4, 2, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (30, 4, 3, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (31, 4, 4, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (32, 4, 5, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (33, 4, 6, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (34, 4, 7, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (35, 4, 8, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (36, 4, 9, 4);

insert into test1355_golf_course_tees (id, course_id, tees_name, tees_color) values (5, 2, 'Blue', 'blue');
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (37, 5, 1, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (38, 5, 2, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (39, 5, 3, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (40, 5, 4, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (41, 5, 5, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (42, 5, 6, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (43, 5, 7, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (44, 5, 8, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (45, 5, 9, 4);

insert into test1355_golf_course_tees (id, course_id, tees_name, tees_color) values (6, 2, 'Red', 'red');
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (46, 6, 1, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (47, 6, 2, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (48, 6, 3, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (49, 6, 4, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (50, 6, 5, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (51, 6, 6, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (52, 6, 7, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (53, 6, 8, 4);
insert into test1355_golf_course_holes (id, course_tees_id, num, par) values (54, 6, 9, 4);
