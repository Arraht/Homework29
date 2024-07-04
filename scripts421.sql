alter table student
add constraint age_constraint CHECK(age>=16);
alter table student
add constraint name_unique unique(name);
alter table faculty
add constraint name_color_unique unique(name, color);
alter table student
alter column age set default 20;
