package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.interfaces.FacultyService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public boolean checkFacultyById(Long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty editFaculty(Long id, Faculty faculty) {
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty foundFacultyForEdit = facultyRepository.findById(id).get();
            foundFacultyForEdit.setFaculty(id, faculty.getName(), faculty.getColor());
            return facultyRepository.save(foundFacultyForEdit);
        }
    }

    @Override
    public Faculty getFaculty(Long id) {
        if (checkFacultyById(id)) {
            return null;
        } else {
            return facultyRepository.findById(id).get();
        }
    }

    @Override
    public List<Faculty> foundFacultyByColor(String color) {
        return facultyRepository
                .findAll()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color.toLowerCase()))
                .toList();
    }

    @Override
    public Collection<Faculty> findFacultyByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Collection<Faculty> findFacultyByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Override
    public Faculty deleteFaculty(Long id) {
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(id).get();
            facultyRepository.deleteById(id);
            return faculty;
        }
    }

    @Override
    public List<Student> findStudentFromFaculty(Long id) {
        return studentRepository.findByFaculty_Id(id);
    }

    @Override
    public Student addStudentByFaculty(Long studentId, Long facultyId) {
        if (studentRepository.findById(studentId).isEmpty()) {
            return null;
        } else if (facultyRepository.findById(facultyId).isEmpty()) {
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(facultyId).get();
            Student student = studentRepository.findById(studentId).get();
            student.setFaculty(faculty);
            return studentRepository.save(student);
        }
    }
}