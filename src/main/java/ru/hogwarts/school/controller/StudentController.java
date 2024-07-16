package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.interfaces.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student.getId(), student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> removeStudent(@PathVariable Long id) {
        Student student = studentService.deleteStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/age")
    public List<Student> getStudentByAge(@RequestParam int age) {
        return studentService.foundStudentByAge(age);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Student>> getStudentByName(@PathVariable String name) {
        List<Student> resultFind = studentService.findStudentByName(name);
        if (resultFind != null) {
            return ResponseEntity.ok(resultFind);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/age/between")
    public Collection<Student> getStudentByAgeBetween(@RequestParam Integer minAge,
                                                      @RequestParam Integer maxAge) {
        return studentService.findStudentBetweenByAge(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> findFacultyFromStudent(@PathVariable Long id) {
        Faculty faculty = studentService.findFacultyFromStudent(id);
        if (faculty != null) {
            return ResponseEntity.ok(faculty);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/amount/all")
    public Long getAmountAllStudent() {
        return studentService.getAmountStudent();
    }

    @GetMapping("/average")
    public Double getAverageStudent() {
        return studentService.findAverageAllStudent();
    }

    @GetMapping("/last/five")
    public List<Student> getLastFiveStudent() {
        return studentService.findLastFiveStudent();
    }

    @GetMapping("/filter/a")
    public ResponseEntity<List<Student>> getStudentByFilterStartsWithA() {
        List<Student> students = studentService.findStudentByStartsWithA().stream().toList();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/average/amount")
    public Double getStudentAverage() {
        return studentService.findAverageStudentByAge();
    }
}