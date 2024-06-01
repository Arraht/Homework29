package ru.hogwarts.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FacultyServiceTest {
    private final FacultyService facultyService = new FacultyService();
    private Faculty gryffindor;
    private Faculty slytherin;
    private Faculty hufflepuff;
    private Faculty ravenclaw;
    private final Map<Long, Faculty> facultyMapTest = new HashMap<>(Map.of());

    @BeforeEach
    public void setUp() {
        gryffindor = new Faculty(1L, "Гриффиндор", "красный");
        slytherin = new Faculty(2L, "Слизерин", "зелёный");
        hufflepuff = new Faculty(3L, "Пуфендуй", "красный");
        ravenclaw = new Faculty(4L, "Когтевран", "синий");
        facultyMapTest.put(1L, gryffindor);
        facultyMapTest.put(2L, slytherin);
        facultyMapTest.put(3L, hufflepuff);
        facultyMapTest.put(4L, ravenclaw);
    }

    @Test
    public void getAllFacultyTest() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertEquals(facultyMapTest, facultyService.getAllFaculty());
    }

    @Test
    public void returnAddFacultyTest() {
        Faculty faculty = new Faculty(1L, "Гриффиндор", "красный");
        assertEquals(gryffindor, facultyService.addFaculty(faculty));
    }

    @Test
    public void addFacultyTest() {
        facultyService.addFaculty(gryffindor);
        facultyService.addFaculty(slytherin);
        facultyService.addFaculty(hufflepuff);
        facultyService.addFaculty(ravenclaw);
        assertEquals(facultyMapTest, facultyService.getAllFaculty());
    }

    @Test
    public void returnEditFacultyTest() {
        Faculty faculty = new Faculty(2L, "Гриффиндор", "красный");
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertEquals(faculty, facultyService.editFaculty(faculty));
    }

    @Test
    public void returnNullEditFacultyTest() {
        Faculty faculty = new Faculty(7L, "Гриффиндор", "красный");
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertNull(facultyService.editFaculty(faculty));
    }

    @Test
    public void editFacultyTest() {
        Faculty faculty = new Faculty(2L, "Гриффиндор", "красный");
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        facultyMapTest.put(2L, faculty);
        facultyService.editFaculty(faculty);
        assertEquals(facultyMapTest, facultyService.getAllFaculty());
    }

    @Test
    public void returnNullGetFacultyTest() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertNull(facultyService.getFaculty(10L));
    }

    @Test
    public void getFacultyTest() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertEquals(hufflepuff, facultyService.getFaculty(3L));
    }

    @Test
    public void returnNullFoundFacultyByColor() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertNull(facultyService.foundFacultyByColor("черный"));
    }

    @Test
    public void foundFacultyByColor() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        facultyMapTest.remove(2L);
        facultyMapTest.remove(4L);
        assertEquals(facultyMapTest.values().stream().toList(), facultyService.foundFacultyByColor("красный"));
    }

    @Test
    public void returnNullDeleteFaculty() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertNull(facultyService.deleteFaculty(10L));
    }

    @Test
    public void returnDeleteFaculty() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        assertEquals(slytherin, facultyService.deleteFaculty(2L));
    }

    @Test
    public void deleteFaculty() {
        facultyService.getAllFaculty().put(1L, gryffindor);
        facultyService.getAllFaculty().put(2L, slytherin);
        facultyService.getAllFaculty().put(3L, hufflepuff);
        facultyService.getAllFaculty().put(4L, ravenclaw);
        facultyMapTest.remove(3L);
        facultyService.deleteFaculty(3L);
        assertEquals(facultyMapTest, facultyService.getAllFaculty());
    }
}