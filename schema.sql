Create database if not exists medicolabosolutions ;

Use medicolabosolutions;

Create table if not exists patient(
patient_id int NOT NULL AUTO_INCREMENT,
firstname varchar(30),
lastname varchar(50),
birth_date date,
gender char,
address varchar(200),
phone_number varchar(12),
PRIMARY KEY (patient_id)) ENGINE=InnoDB;

create table if not exists users(
username varchar(50) not null primary key,
password varchar(500) not null,
enabled boolean not null);

create table if not exists authorities(
username varchar(50) not null,
authority varchar(50) not null,
constraint fk_authorities_users foreign key(username) references users(username));

create unique index ix_auth_username on authorities (username,authority);

insert into patient(firstname, lastname, birth_date, gender, address, phone_number) values
('Test','TestNone','1966-12-31','F','1 Brookside St','100-222-3333'),
('Test','TestBorderline','1945-06-24','M','2 High St','200-333-4444'),
('Test','TestInDanger','2004-06-18','M','3 Club Road','300-444-5555'),
('Test','TestEarlyOnset','2002-06-28','F','4 Valley Dr','400-555-6666');

insert into users(username, password, enabled) values
('doctorUser','$2a$10$4hE5.hwJbB7LBJlNiUk1Tu2ob.k1ZrvH7Y620Dd43ElDiaF/HMOFm', true),
('doctorAdmin','$2a$10$inK6Ze3rMd6cZ6qc3EDdfurPfwl/YjjX.lnbllqSP/8Eo2v01ik2G', true);

insert into authorities(username, authority) values
('doctorUser','USER'),
('doctorAdmin','USER'),
('doctorAdmin','ADMIN');