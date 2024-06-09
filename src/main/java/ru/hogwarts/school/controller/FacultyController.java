package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PostMapping("/add/student")
    public ResponseEntity<Student> addStudentByFaculty(@RequestParam Long studentId,
                                                       @RequestParam Long facultyId) {
        Student student = facultyService.addStudentByFaculty(studentId, facultyId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.deleteFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("color")
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.foundFacultyByColor(color);
    }

    @GetMapping("find")
    public Collection<Faculty> getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String color) {
        if (name != null && !name.isBlank()) {
            return facultyService.findFacultyByName(name);
        } else {
            return facultyService.findFacultyByColor(color);
        }
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty.getId(), faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> findStudentFromFaculty(@PathVariable Long id) {
        List<Student> students = facultyService.findStudentFromFaculty(id);
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }
}