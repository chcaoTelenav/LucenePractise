﻿CREATE TABLE STUDENTS(
   ID SERIAL PRIMARY KEY   NOT NULL,
   FIRSTNAME    CHARACTER(20)    NOT NULL,
   LASTNAME     CHARACTER(20)    NOT NULL,
   GENDER	CHARACTER(8)	NOT NULL,
   AGE INT NOT NULL,
   BIRTHDAY	DATE	NOT NULL,
   PHONENUMBER  TEXT        NOT NULL,
   EMAIL        TEXT,
   DORMITORY	TEXT,
   PERSONALINFO TEXT

);  

INSERT INTO STUDENTS (FIRSTNAME,LASTNAME,GENDER,AGE,BIRTHDAY,PHONENUMBER,EMAIL,DORMITORY,PERSONALINFO) VALUES
('Peter','George','Male', 31,'1986-07-01','6548764563','pg@gmail.com','Room401','like playing football and reading book');

INSERT INTO STUDENTS (FIRSTNAME,LASTNAME,GENDER,AGE,BIRTHDAY,PHONENUMBER,EMAIL,DORMITORY,PERSONALINFO) VALUES
('Mary','Green','Female', 29,'1988-10-21','6438722563' , 'Mary11@gmail.com','Room302','good at singing and dancing');

INSERT INTO STUDENTS (FIRSTNAME,LASTNAME,GENDER,AGE,BIRTHDAY,PHONENUMBER,EMAIL,DORMITORY,PERSONALINFO) VALUES
('Tom','George','Male',26,'1991-02-11','6548764323','Tom77G@gmail.com','Room801', 'like playing football and video game');

INSERT INTO STUDENTS (FIRSTNAME,LASTNAME,GENDER,AGE,BIRTHDAY,PHONENUMBER,EMAIL,DORMITORY,PERSONALINFO) VALUES
('Jerry','Bao','Male',28,'1989-07-01','6548678463','Jb9382@gmail.com','Room401', 'like traveling and eating');

INSERT INTO STUDENTS (FIRSTNAME,LASTNAME,GENDER,AGE,BIRTHDAY,PHONENUMBER,EMAIL,DORMITORY,PERSONALINFO) VALUES
('Crystal','Lee','Female',25,'1992-11-21','6328764563','crystal23@gmail.com','Room201', 'like shopping and traveling');
