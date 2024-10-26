Create database if not exists medicolabosolutions;

Use medicolabosolutions;

Create table if not exists patient(
patient_id int NOT NULL AUTO_INCREMENT,
firstname varchar(30),
lastname varchar(50),
birth_date date,
gender varchar(1),
address varchar(200),
phone_number varchar(12),
PRIMARY KEY (patient_id)) ENGINE=InnoDB;

create unique index ix_firstname_lastname on patient (firstname,lastname);

insert into patient(firstname, lastname, birth_date, gender, address, phone_number) values
('Test','TestNone','1966-12-31','F','1 Brookside St','100-222-3333'),
('Test','TestBorderline','1945-06-24','M','2 High St','200-333-4444'),
('Test','TestInDanger','2004-06-18','M','3 Club Road','300-444-5555'),
('Test','TestEarlyOnset','2002-06-28','F','4 Valley Dr','400-555-6666');