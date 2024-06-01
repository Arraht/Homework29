package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student editStudent(Long id, Student student) {
        if (studentRepository.findById(id).isEmpty()) {
            return null;
        }
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        if (studentRepository.findById(id).isEmpty()) {
            return null;
        }
        return studentRepository.findById(id).get();
    }

    public List<Student> foundStudentByAge(int age) {
        List<Student> studentList = studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .toList();
        if (studentList.isEmpty()) {
            return null;
        }
        return studentList;
    }

    public Student deleteStudent(Long id) {
        if (studentRepository.findById(id).isEmpty()) {
            return null;
        } else {
            Student student = studentRepository.findById(id).get();
            studentRepository.deleteById(id);
            return student;
        }
    }
}