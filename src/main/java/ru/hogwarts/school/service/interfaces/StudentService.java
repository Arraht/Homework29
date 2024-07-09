package ru.hogwarts.school.service.interfaces;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    boolean checkStudentById(Long id);

    Student addStudent(Student student);

    Student editStudent(Long id, Student student);

    Student findStudent(Long id);

    List<Student> findStudentByName(String name);

    List<Student> foundStudentByAge(int age);

    Student deleteStudent(Long id);

    Collection<Student> findStudentBetweenByAge(Integer minAge, Integer maxAge);

    Faculty findFacultyFromStudent(Long id);

    Long getAmountStudent();

    Double findAverageAllStudent();

    List<Student> findLastFiveStudent();
}