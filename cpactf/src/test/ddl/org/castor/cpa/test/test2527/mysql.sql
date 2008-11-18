DROP TABLE IF EXISTS TEST2527_LOG_REFERENCE;
DROP TABLE IF EXISTS TEST2527_LOG_EXCEPTION;
DROP TABLE IF EXISTS TEST2527_LOG;


CREATE TABLE TEST2527_LOG (
            ID         INT NOT NULL,
            STAMP      TIMESTAMP NOT NULL,
            SOURCE     VARCHAR(100) NOT NULL,  
            LLEVEL     VARCHAR(10) NOT NULL,  
            MESSAGE    VARCHAR(500) NULL
);  

ALTER TABLE TEST2527_LOG ADD PRIMARY KEY (ID);


CREATE TABLE TEST2527_LOG_EXCEPTION (
            ID         INT NOT NULL,
            ENTRY_ID   INT NOT NULL,
            STACKTRACE VARCHAR(2000) NOT NULL
);

ALTER TABLE TEST2527_LOG_EXCEPTION ADD PRIMARY KEY (ID);

ALTER TABLE TEST2527_LOG_EXCEPTION
ADD CONSTRAINT TEST2527_LOG_EXCEPTION_FK
FOREIGN KEY (ENTRY_ID) REFERENCES TEST2527_LOG (ID);


CREATE TABLE TEST2527_LOG_REFERENCE (
            ID         INT NOT NULL,
            TYPE       VARCHAR(100) NOT NULL,
            VALUE      VARCHAR(100) NOT NULL
);

ALTER TABLE TEST2527_LOG_REFERENCE ADD PRIMARY KEY (ID);

ALTER TABLE TEST2527_LOG_REFERENCE
ADD CONSTRAINT TEST2527_LOG_REFERENZ_FK
FOREIGN KEY (ID) REFERENCES TEST2527_LOG (ID);


INSERT INTO TEST2527_LOG VALUES (1, '2008-12-31 12:34:56', 'test1', 'level', 'simple log entry 1');
INSERT INTO TEST2527_LOG VALUES (2, '2008-12-31 12:34:56', 'test1', 'level', 'simple log entry 2');

INSERT INTO TEST2527_LOG VALUES (3, '2008-12-31 12:34:56', 'test2', 'level', 'exception log entry 1');
INSERT INTO TEST2527_LOG_EXCEPTION VALUES (1, 3, 'stacktrace for exception log entry 1');
INSERT INTO TEST2527_LOG VALUES (4, '2008-12-31 12:34:56', 'test2', 'level', 'exception log entry 2');
INSERT INTO TEST2527_LOG_EXCEPTION VALUES (2, 4, 'stacktrace for exception log entry 2');

INSERT INTO TEST2527_LOG VALUES (5, '2008-12-31 12:34:56', 'test3', 'level', 'refering log entry 1');
INSERT INTO TEST2527_LOG_REFERENCE VALUES (5, 'type', 'ref 1');
INSERT INTO TEST2527_LOG VALUES (6, '2008-12-31 12:34:56', 'test3', 'level', 'refering log entry 2');
INSERT INTO TEST2527_LOG_REFERENCE VALUES (6, 'type', 'ref 2');

INSERT INTO TEST2527_LOG VALUES (7, '2008-12-31 12:34:56', 'test4', 'level', 'simple and exception log entry 1');
INSERT INTO TEST2527_LOG VALUES (8, '2008-12-31 12:34:56', 'test4', 'level', 'simple and exception log entry 2');
INSERT INTO TEST2527_LOG_EXCEPTION VALUES (3, 8, 'stacktrace for simple and exception log entry 2');
INSERT INTO TEST2527_LOG VALUES (9, '2008-12-31 12:34:56', 'test4', 'level', 'simple and exception log entry 3');

INSERT INTO TEST2527_LOG VALUES (10, '2008-12-31 12:34:56', 'test5', 'level', 'simple and refering log entry 1');
INSERT INTO TEST2527_LOG VALUES (11, '2008-12-31 12:34:56', 'test5', 'level', 'simple and refering log entry 2');
INSERT INTO TEST2527_LOG_REFERENCE VALUES (11, 'type', 's+r 2');
INSERT INTO TEST2527_LOG VALUES (12, '2008-12-31 12:34:56', 'test5', 'level', 'simple and refering log entry 3');

INSERT INTO TEST2527_LOG VALUES (13, '2008-12-31 12:34:56', 'test6', 'level', 'exception, refering and simple log entry 1');
INSERT INTO TEST2527_LOG_EXCEPTION VALUES (4, 13, 'stacktrace for exception, refering and simple log entry 1');
INSERT INTO TEST2527_LOG VALUES (14, '2008-12-31 12:34:56', 'test6', 'level', 'exception, refering and simple log entry 2');
INSERT INTO TEST2527_LOG_REFERENCE VALUES (14, 'type', 'e+r+s 2');
INSERT INTO TEST2527_LOG VALUES (15, '2008-12-31 12:34:56', 'test6', 'level', 'exception, refering and simple log entry 3');
