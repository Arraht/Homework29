package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyService facultyService;
    private Faculty faculty;

    @BeforeEach
    public void setUp() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гиффиндор");
        faculty.setColor("Красный");
    }

    @Test
    public void addFacultyTest() {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        assertEquals("Гиффиндор", facultyService.addFaculty(faculty).getName());
    }

    @Test
    public void checkFacultyByIdTrueTest() {
        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());
        assertTrue(facultyService.checkFacultyById(2L));
    }

    @Test
    public void checkFacultyByIdTrueFalse() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        assertFalse(facultyService.checkFacultyById(1L));
    }

    @Test
    public void editFacultyTest() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        faculty.setName("Слизерин");
        assertEquals("Слизерин", facultyService.editFaculty(1L, faculty).getName());
    }

    @Test
    public void returnNullEditFacultyTest() {
        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(facultyService.editFaculty(2L, faculty));
    }

    @Test
    public void getFacultyTest() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        assertEquals("Гиффиндор", facultyService.getFaculty(1L).getName());
    }

    @Test
    public void returnNullGetFacultyTest() {
        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(facultyService.getFaculty(2L));
    }

    @Test
    public void foundFacultyByColorSizeTest() {
        Faculty facultyTest = new Faculty(2L, "Слизерин", "Зелёный");
        List<Faculty> faculties = new ArrayList<>(List.of(faculty, facultyTest));
        when(facultyRepository.findAll()).thenReturn(faculties);
        assertEquals(1, facultyService.foundFacultyByColor("красный").size());
    }

    @Test
    public void foundFacultyByColorTest() {
        Faculty facultyTest = new Faculty(2L, "Слизерин", "Зелёный");
        List<Faculty> faculties = new ArrayList<>(List.of(faculty, facultyTest));
        when(facultyRepository.findAll()).thenReturn(faculties);
        assertEquals("Гиффиндор", facultyService.foundFacultyByColor("красный").get(0).getName());
    }

    @Test
    public void emptyFoundFacultyByColorTest() {
        Faculty facultyTest = new Faculty(2L, "Слизерин", "Зелёный");
        List<Faculty> faculties = new ArrayList<>(List.of(faculty, facultyTest));
        when(facultyRepository.findAll()).thenReturn(faculties);
        assertTrue(facultyService.foundFacultyByColor("белый").isEmpty());
    }

    @Test
    public void returnDeleteFacultyTest() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        assertEquals("Гиффиндор", facultyService.deleteFaculty(1L).getName());
    }

    @Test
    public void returnNullDeleteFacultyTest() {
        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(facultyService.deleteFaculty(2L));
    }

    @Test
    public void deleteFacultyTest() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        facultyService.deleteFaculty(1L);
        verify(facultyRepository, times(1)).deleteById(1L);
    }
}