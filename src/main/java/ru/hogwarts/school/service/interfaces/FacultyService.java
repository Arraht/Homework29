package ru.hogwarts.school.service.interfaces;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface FacultyService {
    Faculty addFaculty(Faculty faculty);

    Faculty editFaculty(Long id, Faculty faculty);

    Faculty getFaculty(Long id);

    List<Faculty> foundFacultyByColor(String color);

    Collection<Faculty> findFacultyByName(String name);

    Collection<Faculty> findFacultyByColor(String color);

    Faculty deleteFaculty(Long id);

    List<Student> findStudentFromFaculty(Long id);

    Student addStudentByFaculty(Long studentId, Long facultyId);
}