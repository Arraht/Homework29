package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.interfaces.StudentService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean checkStudentById(Long id) {
        logger.debug("проверка существования студента по его id");
        return studentRepository.findById(id).isEmpty();
    }

    @Override
    public Student addStudent(Student student) {
        logger.info("Вызов метода для добавления студента : {} в базу данных", student.getName());
        if (student.getAge() == 0) {
            logger.debug("Возраст для студента : {} установлен по дефолту", student.getName());
            student.setAge(null);
            logger.info("Студент : {} был добавлен в БД", student.getName());
            return studentRepository.save(student);
        } else {
            logger.info("Студент : {} был успешно добавлен в БД", student.getName());
            return studentRepository.save(student);
        }
    }

    @Override
    public Student editStudent(Long id, Student student) {
        logger.info("Вызов метода редактирования студента : {}", student.getName());
        if (checkStudentById(id)) {
            logger.warn("Студента : {} нет в БД", student.getName());
            return null;
        } else {
            Student foundStudentForEdit = studentRepository.findById(id).get();
            foundStudentForEdit.setStudent(id, student.getName(), student.getAge());
            logger.info("Студент : {} был изменён на : {}", student, foundStudentForEdit);
            return studentRepository.save(foundStudentForEdit);
        }
    }

    @Override
    public Student findStudent(Long id) {
        logger.info("Вызов метода для поиска студента по id");
        if (checkStudentById(id)) {
            logger.warn("По id : {} нет студента в БД", id);
            return null;
        } else {
            logger.info("Найден студент по id : {}", id);
            return studentRepository.findById(id).get();
        }
    }

    @Override
    public List<Student> findStudentByName(String name) {
        logger.info("Вызов метода поиска студента по имени : {}", name);
        List<Student> result = studentRepository.findByNameIgnoreCase(name);
        if (!result.isEmpty()) {
            logger.info("Найдены все совпадения поиска по имени : {}", name);
            return studentRepository.findByNameIgnoreCase(name);
        } else {
            logger.warn("Не найдено ни одного совпадения по имени : {}", name);
            return null;
        }
    }

    @Override
    public List<Student> foundStudentByAge(int age) {
        logger.info("Вызов метода поиска студентов по возрасту : {} лет", age);
        return studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

    @Override
    public Student deleteStudent(Long id) {
        logger.info("Вызов метода для удаления студента по его id : {}", id);
        if (checkStudentById(id)) {
            logger.warn("Нет студента в бд с таким id : {}", id);
            return null;
        } else {
            Student student = studentRepository.findById(id).get();
            logger.info("Студент : {}, с id : {} удалён", student.getName(), id);
            studentRepository.deleteById(id);
            return student;
        }
    }

    @Override
    public Collection<Student> findStudentBetweenByAge(Integer minAge, Integer maxAge) {
        logger.info("Вызов метода для поиска студентов по диапазону возрастов от : {} лет, до : {}, лет", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty findFacultyFromStudent(Long id) {
        logger.info("Вызов метода для поиска факультета студента с id : {}", id);
        Student student = findStudent(id);
        if (student != null) {
            Faculty faculty = student.getFaculty();
            logger.info("Факультет для студента по id : {}, найден : {}", id, student);
            return faculty;
        } else {
            return null;
        }
    }

    @Override
    public Long getAmountStudent() {
        logger.info("Вызов метода для подсчёта всех студентов");
        return studentRepository.getAmountStudent();
    }

    @Override
    public Double findAverageAllStudent() {
        logger.info("Вызов метода для поиска среднего возраста студентов");
        return studentRepository.getAverageAllStudent();
    }

    @Override
    public List<Student> findLastFiveStudent() {
        logger.info("Вызов метода для поиска последних пяти студентов");
        return studentRepository.getLastFiveStudent();
    }

    @Override
    public Collection<Student> findStudentByStartsWithA() {
        logger.info("Вызов метода поиска студентов по имени, начинающегося на А");
        return studentRepository.findAll()
                .stream()
                .filter(a -> a.getName()
                        .toUpperCase()
                        .startsWith("А"))
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    @Override
    public Double findAverageStudentByAge() {
        logger.info("Вызов метода подсчёта среднего значения возраста студентов");
        return studentRepository.findAll()
                .stream()
                .map(Student::getAge)
                .mapToInt(s -> s)
                .average()
                .orElseThrow();
    }
}