package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    List<Student> findByFaculty_Id(Long facultyId);

    List<Student> findByNameIgnoreCase(String name);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Long getAmountStudent();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getAverageAllStudent();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudent();
}