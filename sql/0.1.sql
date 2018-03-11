-- CREATE DB
CREATE DATABASE parser;

-- CREATE TABLES
CREATE TABLE records (
        id INT NOT NULL AUTO_INCREMENT,
        date VARCHAR(30) NOT NULL,
        ip VARCHAR(30),
        request VARCHAR(100) NOT NULL,
        status DATE NOT NULL,
        user_agent VARCHAR(40) NOT NULL,
        PRIMARY KEY (id)
    );