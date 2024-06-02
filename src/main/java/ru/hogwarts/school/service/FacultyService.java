package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.List;
import java.util.Objects;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public boolean checkFacultyById(Long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty editFaculty(Long id, Faculty faculty) {
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty foundFacultyForEdit = facultyRepository.findById(id).get();
            foundFacultyForEdit.setFaculty(id, faculty.getName(), faculty.getColor());
            return facultyRepository.save(foundFacultyForEdit);
        }
    }

    public Faculty getFaculty(Long id) {
        if (checkFacultyById(id)) {
            return null;
        } else {
            return facultyRepository.findById(id).get();
        }
    }

    public List<Faculty> foundFacultyByColor(String color) {
        return facultyRepository
                .findAll()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color.toLowerCase()))
                .toList();
    }

    public Faculty deleteFaculty(Long id) {
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(id).get();
            facultyRepository.deleteById(id);
            return faculty;
        }
    }
}