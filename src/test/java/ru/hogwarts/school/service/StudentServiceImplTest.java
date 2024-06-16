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
public class StudentServiceImplTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentServiceImpl;
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
        assertEquals("Гарри Поттер", studentServiceImpl.addStudent(student).getName());
    }

    @Test
    public void editStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        student.setName("Фред Уизли");
        Student studentTest = studentServiceImpl.editStudent(1L, student);
        assertEquals("Фред Уизли", studentTest.getName());
    }

    @Test
    public void checkStudentByIdTrueTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertTrue(studentServiceImpl.checkStudentById(2L));
    }

    @Test
    public void checkStudentByIdFalseTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertFalse(studentServiceImpl.checkStudentById(1L));
    }

    @Test
    public void returnNullEditStudentTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(studentServiceImpl.editStudent(2L, student));
    }

    @Test
    public void findStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertEquals("Гарри Поттер", studentServiceImpl.findStudent(1L).getName());
    }

    @Test
    public void returnNullFindStudentTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(studentServiceImpl.findStudent(2L));
    }

    @Test
    public void foundSizeStudentByAgeTest() {
        Student studentTest = new Student(2L, "Фред Уизли", 13);
        List<Student> students = new ArrayList<>(List.of(student, studentTest));
        when(studentRepository.findAll()).thenReturn(students);
        assertEquals(1, studentServiceImpl.foundStudentByAge(11).size());
    }

    @Test
    public void foundStudentByAgeTest() {
        Student studentTest = new Student(2L, "Фред Уизли", 13);
        List<Student> students = new ArrayList<>(List.of(student, studentTest));
        when(studentRepository.findAll()).thenReturn(students);
        assertEquals("Гарри Поттер", studentServiceImpl.foundStudentByAge(11).get(0).getName());
    }

    @Test
    public void emptyFoundStudentByAgeTest() {
        Student studentTest = new Student(2L, "Фред Уизли", 13);
        List<Student> students = new ArrayList<>(List.of(student, studentTest));
        when(studentRepository.findAll()).thenReturn(students);
        assertTrue(studentServiceImpl.foundStudentByAge(15).isEmpty());
    }

    @Test
    public void returnDeleteStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertEquals("Гарри Поттер", studentServiceImpl.deleteStudent(1L).getName());
    }

    @Test
    public void returnNullDeleteStudentTest() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(studentServiceImpl.deleteStudent(2L));
    }

    @Test
    public void deleteStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        studentServiceImpl.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}