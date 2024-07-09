create table cars(
cars_id int primary key,
brand varchar(255),
price decimal(10, 2)
);

create table people(
people_id int primary key,
name varchar(255),
age int,
has_license boolean,
cars_id int references cars (cars_id)
);