package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> allStudents = new HashMap<>(Map.of());
    private Long id = 0L;

    public Student addStudent(Student student) {
        ++this.id;
        student.setId(this.id);
        allStudents.put(student.getId(), student);
        return student;
    }

    public Student editStudent(Student student) {
        if (allStudents.containsKey(student.getId())) {
            allStudents.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student findStudent(Long id) {
        if (allStudents.containsKey(id)) {
            return allStudents.get(id);
        }
        return null;
    }

    public List<Student> foundStudentByAge(int age) {
        List<Student> studentByAge =  allStudents
                .values()
                .stream()
                .filter(student -> student.getAge() == age)
                .toList();
        if (studentByAge.isEmpty()) {
            return null;
        }
        return studentByAge;
    }


    public Student deleteStudent(Long id) {
        if (allStudents.containsKey(id)) {
            return allStudents.remove(id);
        }
        return null;
    }
}