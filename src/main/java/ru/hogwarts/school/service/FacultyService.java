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

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty editFaculty(Long id, Faculty faculty) {
        if (facultyRepository.findById(id).isEmpty()) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            return null;
        }
        return facultyRepository.findById(id).get();
    }

    public List<Faculty> foundFacultyByColor(String color) {
        List<Faculty> facultyList = facultyRepository
                .findAll()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .toList();
        if (facultyList.isEmpty()) {
            return null;
        } else {
            return facultyList;
        }
    }

    public Faculty deleteFaculty(Long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(id).get();
            facultyRepository.deleteById(id);
            return faculty;
        }
    }
}