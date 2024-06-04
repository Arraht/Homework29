package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(11);
    }


    @Test
    public void AddStudentTest() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        assertEquals("Гарри Поттер", studentService.addStudent(student).getName());
    }

    @Test
    public void editStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        student.setName("Фред Уизли");
        Student studentTest = studentService.editStudent(1L, student);
        assertEquals("Фред Уизли", studentTest.getName());
    }

    @Test
    public void checkStudentByIdTrueTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertTrue(studentService.checkStudentById(2L));
    }

    @Test
    public void checkStudentByIdFalseTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertFalse(studentService.checkStudentById(1L));
    }

    @Test
    public void returnNullEditStudentTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(studentService.editStudent(2L, student));
    }

    @Test
    public void findStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertEquals("Гарри Поттер", studentService.findStudent(1L).getName());
    }

    @Test
    public void returnNullFindStudentTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(studentService.findStudent(2L));
    }

    @Test
    public void foundSizeStudentByAgeTest() {
        Student studentTest = new Student(2L, "Фред Уизли", 13);
        List<Student> students = new ArrayList<>(List.of(student, studentTest));
        when(studentRepository.findAll()).thenReturn(students);
        assertEquals(1, studentService.foundStudentByAge(11).size());
    }

    @Test
    public void foundStudentByAgeTest() {
        Student studentTest = new Student(2L, "Фред Уизли", 13);
        List<Student> students = new ArrayList<>(List.of(student, studentTest));
        when(studentRepository.findAll()).thenReturn(students);
        assertEquals("Гарри Поттер", studentService.foundStudentByAge(11).get(0).getName());
    }

    @Test
    public void emptyFoundStudentByAgeTest() {
        Student studentTest = new Student(2L, "Фред Уизли", 13);
        List<Student> students = new ArrayList<>(List.of(student, studentTest));
        when(studentRepository.findAll()).thenReturn(students);
        assertTrue(studentService.foundStudentByAge(15).isEmpty());
    }

    @Test
    public void returnDeleteStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertEquals("Гарри Поттер", studentService.deleteStudent(1L).getName());
    }

    @Test
    public void returnNullDeleteStudentTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(studentService.deleteStudent(2L));
    }

    @Test
    public void deleteStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        studentService.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}