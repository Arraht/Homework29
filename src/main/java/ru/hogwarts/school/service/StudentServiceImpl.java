package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.interfaces.StudentService;

import java.util.Collection;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean checkStudentById(Long id) {
        return studentRepository.findById(id).isEmpty();
    }

    @Override
    public Student addStudent(Student student) {
        if (student.getAge() == 0) {
            student.setAge(null);
            return studentRepository.save(student);
        } else {
            return studentRepository.save(student);        }
    }

    @Override
    public Student editStudent(Long id, Student student) {
        if (checkStudentById(id)) {
            return null;
        } else {
            Student foundStudentForEdit = studentRepository.findById(id).get();
            foundStudentForEdit.setStudent(id, student.getName(), student.getAge());
            return studentRepository.save(foundStudentForEdit);
        }
    }

    @Override
    public Student findStudent(Long id) {
        if (checkStudentById(id)) {
            return null;
        } else {
            return studentRepository.findById(id).get();
        }
    }

    @Override
    public List<Student> foundStudentByAge(int age) {
        return studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

    @Override
    public Student deleteStudent(Long id) {
        if (checkStudentById(id)) {
            return null;
        } else {
            Student student = studentRepository.findById(id).get();
            studentRepository.deleteById(id);
            return student;
        }
    }

    @Override
    public Collection<Student> findStudentBetweenByAge(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty findFacultyFromStudent(Long id) {
        return findStudent(id).getFaculty();
    }

    @Override
    public Long getAmountStudent() {
        return studentRepository.getAmountStudent();
    }

    @Override
    public Double findAverageAllStudent() {
        return studentRepository.getAverageAllStudent();
    }

    @Override
    public List<Student> findLastFiveStudent() {
        return studentRepository.getLastFiveStudent();
    }
}