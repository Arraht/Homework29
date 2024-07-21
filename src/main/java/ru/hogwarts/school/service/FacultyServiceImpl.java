package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.interfaces.FacultyService;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public boolean checkFacultyById(Long id) {
        logger.debug("Вызов метода проверки существования факультета по id : {}", id);
        if (facultyRepository.findById(id).isEmpty()) {
            logger.warn("Факультета по id : {} нет", id);
            return true;
        } else {
            logger.debug("Факультет по id : {} найден", id);
            return false;
        }
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Вызов метода для сохранения факультета в БД");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty editFaculty(Long id, Faculty faculty) {
        logger.info("Вызов метода для изменения факультета : {}", faculty.getName());
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty foundFacultyForEdit = facultyRepository.findById(id).get();
            foundFacultyForEdit.setFaculty(id, faculty.getName(), faculty.getColor());
            logger.info("Факультет {} был изменён на {}", faculty, foundFacultyForEdit);
            return facultyRepository.save(foundFacultyForEdit);
        }
    }

    @Override
    public Faculty getFaculty(Long id) {
        logger.info("Вызов метода для поиска факультета по id {}", id);
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(id).get();
            logger.info("По id {} был найдет факультет {}", id, faculty);
            return faculty;
        }
    }

    @Override
    public List<Faculty> foundFacultyByColor(String color) {
        logger.info("Вызов метода поиска факультетов по цвету {}", color);
        return facultyRepository
                .findAll()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color.toLowerCase()))
                .toList();
    }

    @Override
    public Collection<Faculty> findFacultyByName(String name) {
        logger.info("Вызов метода поиска факультета по названию {}", name);
        return facultyRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Collection<Faculty> findFacultyNameAndColor(String name, String color) {
        logger.info("Вызов метода поиска факультета по названию {} и цвету {}", name, color);
        Collection<Faculty> result = facultyRepository.findAllByNameIgnoreCaseAndColorIgnoreCase(name, color);
        if (!result.isEmpty()) {
            logger.info("По названию {} и цвету {} найдены результаты {}", name, color, result);
            return result;
        } else {
            logger.warn("Не найдено ни одно совпадения по названию {} и цвету {}", name, color);
            return null;
        }
    }

    @Override
    public Collection<Faculty> findFacultyByColor(String color) {
        logger.info("Вызов метода поиска факультета по цвету {}", color);
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Override
    public Faculty deleteFaculty(Long id) {
        logger.info("Вызов метода удаления факультета по id {}", id);
        if (checkFacultyById(id)) {
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(id).get();
            logger.info("Факультет {} по id {} был удалён", faculty, id);
            facultyRepository.deleteById(id);
            return faculty;
        }
    }

    @Override
    public List<Student> findStudentFromFaculty(Long id) {
        logger.info("Вызов метода поиска всех студентов факультета по его id {}", id);
        return studentRepository.findByFaculty_Id(id);
    }

    @Override
    public Student addStudentByFaculty(Long studentId, Long facultyId) {
        logger.info("Вызов метода добавления студента id {} на факультет id {}", studentId, facultyId);
        if (studentRepository.findById(studentId).isEmpty()) {
            logger.warn("Студента с id {} нет в БД", studentId);
            return null;
        } else if (facultyRepository.findById(facultyId).isEmpty()) {
            logger.warn("Факультета с id {} нет в БД", facultyId);
            return null;
        } else {
            Faculty faculty = facultyRepository.findById(facultyId).get();
            Student student = studentRepository.findById(studentId).get();
            student.setFaculty(faculty);
            logger.info("Студент {} был добавлен на факультет {}", student.getName(), faculty.getName());
            return studentRepository.save(student);
        }
    }

    @Override
    public Optional<String> findFacultyNameLongest() {
        return facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length));
    }
}