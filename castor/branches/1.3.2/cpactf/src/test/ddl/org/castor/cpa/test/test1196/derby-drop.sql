DROP TABLE test1196_state;
DROP TABLE test1196_country;

ALTER TABLE test1196_car DROP FOREIGN KEY test1196_driver_fk;
ALTER TABLE test1196_driver DROP FOREIGN KEY test1196_car_fk;

DROP TABLE test1196_car;
DROP TABLE test1196_driver;

DROP TABLE test1196_computer;
DROP TABLE test1196_orderitem;
DROP TABLE test1196_product;
DROP TABLE test1196_order;
