CREATE DATABASE IF NOT EXISTS SDM_HW1 ;
USE SDM_HW1;

DROP TABLE IF EXISTS credit_cards;

DROP TABLE IF EXISTS persons;

DROP TABLE IF EXISTS addresses;


CREATE TABLE addresses (
    id INT NOT NULL AUTO_INCREMENT,
    city VARCHAR(45) NOT NULL,
    street VARCHAR(45) NULL,
    country VARCHAR(45) NOT NULL DEFAULT 'Romania',
    PRIMARY KEY (id)
);


CREATE TABLE persons (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  address INT NULL,
  job VARCHAR(45) NOT NULL DEFAULT 'Unemployed',
  birth_date DATE NOT NULL,
  country VARCHAR(45) NOT NULL DEFAULT 'Romania',
  PRIMARY KEY (id),
   CONSTRAINT fk_address
    FOREIGN KEY (address)
    REFERENCES addresses (id));

CREATE TABLE credit_cards (
                              id INT NOT NULL AUTO_INCREMENT,
                              IBAN VARCHAR(45) NOT NULL,
                              amount DOUBLE NOT NULL,
                              person_id INT NOT NULL,
                              PRIMARY KEY (id),
                              CONSTRAINT fk_person_id
                                  FOREIGN KEY (person_id)
                                      REFERENCES persons (id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE
);
