package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public boolean checkStudentById(Long id) {
        return studentRepository.findById(id).isEmpty();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student editStudent(Long id, Student student) {
        if (checkStudentById(id)) {
            return null;
        } else {
            Student foundStudentForEdit = studentRepository.findById(id).get();
            foundStudentForEdit.setStudent(id, student.getName(), student.getAge());
            return studentRepository.save(foundStudentForEdit);
        }
    }

    public Student findStudent(Long id) {
        if (checkStudentById(id)) {
            return null;
        } else {
            return studentRepository.findById(id).get();
        }
    }

    public List<Student> foundStudentByAge(int age) {
        return studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

    public Student deleteStudent(Long id) {
        if (checkStudentById(id)) {
            return null;
        } else {
            Student student = studentRepository.findById(id).get();
            studentRepository.deleteById(id);
            return student;
        }
    }

    public Collection<Student> findStudentBetweenByAge(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFacultyFromStudent(Long id) {
        return findStudent(id).getFaculty();
    }
}