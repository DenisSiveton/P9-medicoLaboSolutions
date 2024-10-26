Create table if not exists patient(
patient_id int NOT NULL AUTO_INCREMENT,
firstname varchar(30),
lastname varchar(50),
birth_date date,
gender varchar(1),
address varchar(200),
phone_number varchar(12),
PRIMARY KEY (patient_id)
);

create unique index ix_firstname_lastname on patient (firstname,lastname);