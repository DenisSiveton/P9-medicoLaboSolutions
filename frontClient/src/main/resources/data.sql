Create database if not exists medicolabosolutions;

Use medicolabosolutions;

create table if not exists users(
username varchar(50) not null primary key,
password varchar(500) not null,
enabled boolean not null);

create table if not exists authorities(
username varchar(50) not null,
authority varchar(50) not null,
constraint fk_authorities_users foreign key(username) references users(username));

create unique index ix_auth_username on authorities (username,authority);

insert into users(username, password, enabled) values
('doctorUser','$2a$10$4hE5.hwJbB7LBJlNiUk1Tu2ob.k1ZrvH7Y620Dd43ElDiaF/HMOFm', true),   -- mdp bestDoctor
('doctorAdmin','$2a$10$inK6Ze3rMd6cZ6qc3EDdfurPfwl/YjjX.lnbllqSP/8Eo2v01ik2G', true);  -- mdp bestDoctorAdmin
insert into authorities(username, authority) values
('doctorUser','USER'),
('doctorAdmin','USER'),
('doctorAdmin','ADMIN');