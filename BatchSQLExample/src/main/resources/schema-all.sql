--DROP TABLE people IF EXISTS;
--
--
--CREATE TABLE people  (
--    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
--    first_name VARCHAR(20),
--    last_name VARCHAR(20)
--);

--CREATE DATABASE springbatch; /*!40100 DEFAULT CHARACTER SET utf8 */;

--DROP TABLE IF EXISTS springbatch.user;


CREATE TABLE user (
  id int(11) NOT NULL auto_increment PRIMARY KEY,
  name VARCHAR(20), NOT NULL default ''
);

INSERT INTO tbl_user(id,name) VALUES(1, 'Jack ');
INSERT INTO tbl_user(id,name) VALUES(2, 'Ron');
INSERT INTO tbl_user(id,name) VALUES(3, 'Bob');

