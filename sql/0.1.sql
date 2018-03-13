-- CREATE DB
CREATE DATABASE parser;

-- CREATE TABLE RECORD
CREATE TABLE LOG_RECORD (
        id INT NOT NULL AUTO_INCREMENT,
        date DATETIME NOT NULL,
        ip VARCHAR(20),
        request VARCHAR(50) NOT NULL,
        status INT NOT NULL,
        user_agent VARCHAR(200) NOT NULL,
        PRIMARY KEY (id)
    );
    
-- CREATE TABLE RECORD
CREATE TABLE BLOCKED_IP (
        id INT NOT NULL AUTO_INCREMENT,
        ip VARCHAR(20),
        comment VARCHAR(150) NOT NULL,
        PRIMARY KEY (id)
    );