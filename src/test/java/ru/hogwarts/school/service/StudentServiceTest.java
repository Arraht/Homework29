package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class StudentServiceTest {
   /* private StudentService studentService;
    private StudentRepository studentRepository;
    private Student harry;
    private Student ron;
    private Student fred;
    private final Map<Long, Student> studentMapTest = new HashMap<>(Map.of());

    @BeforeEach
    public void setUp() {
        harry = new Student(1L, "Гарри Поттер", 11);
        ron = new Student(2L, "Рон Уизли", 11);
        fred = new Student(3L, "Фред Уизли", 13);
        studentMapTest.put(1L, harry);
        studentMapTest.put(2L, ron);
        studentMapTest.put(3L, fred);
    }


    @Test
    public void returnAddStudentTest() {

        Student student = new Student(0L, "Гарри Поттер", 11);
        assertEquals(harry, studentService.addStudent(student));
    }*/

    /*@Test
    public void addStudentMapTest() {
        Student student = new Student(0L, "Гарри Поттер", 11);
        Student studentTwo = new Student(0L, "Рон Уизли", 11);
        Student studentThree = new Student(0L, "Фред Уизли", 13);
        studentService.addStudent(student);
        studentService.addStudent(studentTwo);
        studentService.addStudent(studentThree);
        assertEquals(studentMapTest, studentService.getAllStudents());
    }*/

    /*@Test
    public void ReturnNullEditStudentTest() {
        Student student = new Student(0L, "Гермиона Грейнджер", 15);
        assertNull(studentService.editStudent(student));
    }*/

    /*@Test
    public void returnEditStudentTest() {
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        harry = new Student(1L, "Гарри Поттер", 15);
        Student student = new Student(1L, "Гарри Поттер", 15);
        assertEquals(harry, studentService.editStudent(student));
    }*/

    /*@Test
    public void editStudentTest() {
        Student student = new Student(2L, "Гарри Поттер", 13);
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        studentMapTest.put(2L, student);
        studentService.editStudent(student);
        assertEquals(studentMapTest, studentService.getAllStudents());
    }*/

   /* @Test
    public void findStudentTest() {
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        assertEquals(ron, studentService.findStudent(2L));
    }*/

   /* @Test
    public void returnNullFindStudentTest() {
        studentService.addStudent(harry);
        studentService.addStudent(ron);
        studentService.addStudent(fred);
        assertNull(studentService.findStudent(15L));
    }*/

    /*@Test
    public void foundStudentByAgeTest() {
        studentService.addStudent(harry);
        studentService.addStudent(ron);
        studentService.addStudent(fred);
        studentMapTest.remove(3L);
        assertEquals(studentMapTest.values().stream().toList(), studentService.foundStudentByAge(11));
    }*/

    /*@Test
    public void returnNullFoundStudentByAgeTest() {
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        assertNull(studentService.foundStudentByAge(25));
    }*/

   /* @Test
    public void returnDeleteStudentTest() {
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        assertEquals(fred, studentService.deleteStudent(3L));
    }*/

   /* @Test
    public void returnNullDeleteStudentTest() {
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        assertNull(studentService.deleteStudent(15L));
    }*/

   /* @Test
    public void deleteStudentTest() {
        studentService.getAllStudents().put(1L, harry);
        studentService.getAllStudents().put(2L, ron);
        studentService.getAllStudents().put(3L, fred);
        studentMapTest.remove(3L);
        studentService.deleteStudent(3L);
        assertEquals(studentMapTest, studentService.getAllStudents());
    }*/
}