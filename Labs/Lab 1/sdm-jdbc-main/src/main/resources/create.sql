CREATE DATABASE jdbcex;
USE jdbcex;

CREATE TABLE addresses (
  id INT NOT NULL AUTO_INCREMENT,
  city VARCHAR(45) NOT NULL,
  street VARCHAR(45) NULL,
  PRIMARY KEY (id));


CREATE TABLE persons (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  address INT NULL,
  PRIMARY KEY (id),
   CONSTRAINT fk_address
    FOREIGN KEY (address)
    REFERENCES addresses (id));
