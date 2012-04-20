CREATE TABLE test2527_log (
    id         INT NOT NULL,
    time_stamp TIMESTAMP NOT NULL,
    source     VARCHAR(100) NOT NULL,  
    llevel     VARCHAR(10) NOT NULL,  
    message    VARCHAR(500) DEFAULT NULL,
    PRIMARY KEY (id)
)
//

CREATE TABLE test2527_log_exception (
    id         INT NOT NULL,
    entry_id   INT NOT NULL,
    stacktrace LONG BYTE NOT NULL,
    PRIMARY KEY (id)
)
//
ALTER TABLE test2527_log_exception
ADD FOREIGN KEY test2527_log_exception_fk (entry_id) REFERENCES test2527_log (id)
//

CREATE TABLE test2527_log_reference (
    id         INT NOT NULL,
    rtype      VARCHAR(100) NOT NULL,
    rvalue     VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)
//
ALTER TABLE test2527_log_reference
ADD FOREIGN KEY test2527_log_reference_fk (id) REFERENCES test2527_log (id)
//


INSERT INTO test2527_log VALUES (1, '2008-12-31 12:34:56', 'test1', 'level', 'simple log entry 1')
//
INSERT INTO test2527_log VALUES (2, '2008-12-31 12:34:56', 'test1', 'level', 'simple log entry 2')
//

INSERT INTO test2527_log VALUES (3, '2008-12-31 12:34:56', 'test2', 'level', 'exception log entry 1')
//
INSERT INTO test2527_log_exception VALUES (1, 3, 'stacktrace for exception log entry 1')
//
INSERT INTO test2527_log VALUES (4, '2008-12-31 12:34:56', 'test2', 'level', 'exception log entry 2')
//
INSERT INTO test2527_log_exception VALUES (2, 4, 'stacktrace for exception log entry 2')
//

INSERT INTO test2527_log VALUES (5, '2008-12-31 12:34:56', 'test3', 'level', 'refering log entry 1')
//
INSERT INTO test2527_log_reference VALUES (5, 'type', 'ref 1')
//
INSERT INTO test2527_log VALUES (6, '2008-12-31 12:34:56', 'test3', 'level', 'refering log entry 2')
//
INSERT INTO test2527_log_reference VALUES (6, 'type', 'ref 2')
//

INSERT INTO test2527_log VALUES (7, '2008-12-31 12:34:56', 'test4', 'level', 'simple and exception log entry 1')
//
INSERT INTO test2527_log VALUES (8, '2008-12-31 12:34:56', 'test4', 'level', 'simple and exception log entry 2')
//
INSERT INTO test2527_log_exception VALUES (3, 8, 'stacktrace for simple and exception log entry 2')
//
INSERT INTO test2527_log VALUES (9, '2008-12-31 12:34:56', 'test4', 'level', 'simple and exception log entry 3')
//

INSERT INTO test2527_log VALUES (10, '2008-12-31 12:34:56', 'test5', 'level', 'simple and refering log entry 1')
//
INSERT INTO test2527_log VALUES (11, '2008-12-31 12:34:56', 'test5', 'level', 'simple and refering log entry 2')
//
INSERT INTO test2527_log_reference VALUES (11, 'type', 's+r 2')
//
INSERT INTO test2527_log VALUES (12, '2008-12-31 12:34:56', 'test5', 'level', 'simple and refering log entry 3')
//

INSERT INTO test2527_log VALUES (13, '2008-12-31 12:34:56', 'test6', 'level', 'exception, refering and simple log entry 1')
//
INSERT INTO test2527_log_exception VALUES (4, 13, 'stacktrace for exception, refering and simple log entry 1')
//
INSERT INTO test2527_log VALUES (14, '2008-12-31 12:34:56', 'test6', 'level', 'exception, refering and simple log entry 2')
//
INSERT INTO test2527_log_reference VALUES (14, 'type', 'e+r+s 2')
//
INSERT INTO test2527_log VALUES (15, '2008-12-31 12:34:56', 'test6', 'level', 'exception, refering and simple log entry 3')
//
