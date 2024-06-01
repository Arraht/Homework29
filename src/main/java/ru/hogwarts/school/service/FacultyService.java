package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FacultyService {
    private final Map<Long, Faculty> allFaculty = new HashMap<>(Map.of());
    private Long id = 0L;

    public Faculty addFaculty(Faculty faculty) {
        ++this.id;
        faculty.setId(this.id);
        allFaculty.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty editFaculty(Faculty faculty) {
        if (allFaculty.containsKey(faculty.getId())) {
            allFaculty.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty getFaculty(Long id) {
        if (allFaculty.containsKey(id)) {
            return allFaculty.get(id);
        }
        return null;
    }

    public List<Faculty> foundFacultyByColor(String color) {
        List<Faculty> faculties = allFaculty
                .values()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .toList();
        if (faculties.isEmpty()) {
            return null;
        }
        return faculties;
    }

    public Faculty deleteFaculty(Long id) {
        if (allFaculty.containsKey(id)) {
            return allFaculty.remove(id);
        }
        return null;
    }

    public Map<Long, Faculty> getAllFaculty() {
        return allFaculty;
    }
}