select student."name" , student.age , student.faculty_id, faculty."name"
from student  inner join faculty  on student.faculty_id = faculty.id;

select student."name", student.age, student.id, avatar.id
from student inner join avatar on student.id = avatar.student_id;