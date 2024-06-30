package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.AvatarServiceIml;
import ru.hogwarts.school.service.FacultyServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private AvatarRepository avatarRepository;
    @MockBean
    FacultyRepository facultyRepository;
    @SpyBean
    private StudentServiceImpl studentService;
    @SpyBean
    private AvatarServiceIml avatarService;
    @SpyBean
    private FacultyServiceImpl facultyService;
    @InjectMocks
    private FacultyController facultyController;
    private final JSONObject studentObject = new JSONObject();
    private final JSONObject facultyObject = new JSONObject();
    private final Student student = new Student();
    private final Faculty faculty = new Faculty();
    private Long studentId;
    private String studentName;
    private int studentAge;
    private Long facultyId;
    private String facultyName;
    private String facultyColor;

    @BeforeEach
    public void setUp() throws Exception {
        studentId = 12L;
        studentName = "Седрик Диггори";
        studentAge = 15;
        facultyId = 10L;
        facultyName = "Пуфендуй";
        facultyColor = "оранжевый";
        facultyObject.put("id", facultyId);
        facultyObject.put("name", facultyName);
        facultyObject.put("color", facultyColor);
        studentObject.put("id", studentId);
        studentObject.put("name", studentName);
        studentObject.put("age", studentAge);
        student.setStudent(studentId, studentName, studentAge);
        faculty.setFaculty(facultyId, facultyName, facultyColor);
        student.setFaculty(faculty);
    }

    @Test
    public void createFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(facultyId));
    }


    @Test
    public void addStudentByFaculty() throws Exception {
        Student studentOneTest = new Student(1L, "Драко Малфой", 11);
        Faculty facultyOneTest = new Faculty(2L, "Слизерин", "зелёный");
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentOneTest));
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(facultyOneTest));
        when(studentRepository.save(any(Student.class))).thenReturn(studentOneTest);
        mockMvc.perform(MockMvcRequestBuilders.post("/faculty/add/student?studentId=2&facultyId=2")
                        .content(facultyObject.toString())
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(studentOneTest.getId()))
                .andExpect(jsonPath("$.name").value(studentOneTest.getName()))
                .andExpect(jsonPath("$.age").value(studentOneTest.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(facultyOneTest.getId()))
                .andExpect(jsonPath("$.faculty.name").value(facultyOneTest.getName()))
                .andExpect(jsonPath("$.faculty.color").value(facultyOneTest.getColor()));
    }

    @Test
    public void removeFacultyTest() throws Exception {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }

    @Test
    public void getFacultyTest() throws Exception {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }

    @Test
    public void getFacultyByColorTest() throws Exception {
        Faculty facultyOneTest = new Faculty(2L, "Слизерин", "зелёный");
        Faculty facultyTwoTest = new Faculty(3L, "Когтевран", "зелёный");
        List<Faculty> facultyList = new ArrayList<>(List.of(faculty, facultyOneTest, facultyTwoTest));
        List<Faculty> faculties = facultyList
                .stream()
                .filter(faculty1 -> Objects.equals(faculty1.getColor(), "зелёный"))
                .toList();
        when(facultyRepository.findAll()).thenReturn(faculties);
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/color?color=зелёный")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(facultyOneTest.getId()))
                .andExpect(jsonPath("$.[0].name").value(facultyOneTest.getName()))
                .andExpect(jsonPath("$.[0].color").value(facultyOneTest.getColor()))
                .andExpect(jsonPath("$.[1].id").value(facultyTwoTest.getId()))
                .andExpect(jsonPath("$.[1].name").value(facultyTwoTest.getName()))
                .andExpect(jsonPath("$.[1].color").value(facultyTwoTest.getColor()));
    }

    @Test
    public void getFacultyByNameOrColorTest() throws Exception {
        Faculty facultyOneTest = new Faculty(2L, "Слизерин", "зелёный");
        Faculty facultyTwoTest = new Faculty(3L, "Когтевран", "зелёный");
        List<Faculty> facultyList = new ArrayList<>(List.of(faculty, facultyOneTest, facultyTwoTest));
        List<Faculty> facultiesByColor = facultyList
                .stream()
                .filter(faculty1 -> Objects.equals(faculty1.getColor(), "зелёный"))
                .toList();
        List<Faculty> facultiesByName = facultyList
                .stream()
                .filter(faculty1 -> Objects.equals(faculty1.getName(), "Слизерин"))
                .toList();
        when(facultyRepository.findByNameIgnoreCase(anyString())).thenReturn(facultiesByName);
        when(facultyRepository.findByColorIgnoreCase(anyString())).thenReturn(facultiesByColor);
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/find?name=Слизерин")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(facultyOneTest.getId()))
                .andExpect(jsonPath("$.[0].name").value(facultyOneTest.getName()))
                .andExpect(jsonPath("$.[0].color").value(facultyOneTest.getColor()));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/find?color=зелёный")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(facultyOneTest.getId()))
                .andExpect(jsonPath("$.[0].name").value(facultyOneTest.getName()))
                .andExpect(jsonPath("$.[0].color").value(facultyOneTest.getColor()))
                .andExpect(jsonPath("$.[1].id").value(facultyTwoTest.getId()))
                .andExpect(jsonPath("$.[1].name").value(facultyTwoTest.getName()))
                .andExpect(jsonPath("$.[1].color").value(facultyTwoTest.getColor()));
    }

    @Test
    public void editFacultyTest() throws Exception {
        Faculty facultyOneTest = new Faculty(2L, "Слизерин", "зелёный");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyOneTest);
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(facultyOneTest.getId()))
                .andExpect(jsonPath("$.name").value(facultyOneTest.getName()))
                .andExpect(jsonPath("$.color").value(facultyOneTest.getColor()));
    }

    @Test
    public void findStudentFromFacultyTest() throws Exception {
        Faculty facultyOneTest = new Faculty(2L, "Слизерин", "зелёный");
        Student studentOne = new Student(2L, "Гарри Поттер", 11);
        studentOne.setFaculty(facultyOneTest);
        Student studentTwo = new Student(1L, "Рон Уизли", 17);
        studentTwo.setFaculty(facultyOneTest);
        List<Student> studentList = new ArrayList<>(List.of(studentOne, student, studentTwo));
        List<Student> students = studentList
                .stream()
                .filter(student1 -> student1.getFaculty() == facultyOneTest)
                .toList();
        when(studentRepository.findByFaculty_Id(anyLong())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/2/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(studentOne.getId()))
                .andExpect(jsonPath("$.[0].name").value(studentOne.getName()))
                .andExpect(jsonPath("$.[0].age").value(studentOne.getAge()))
                .andExpect(jsonPath("$.[1].id").value(studentTwo.getId()))
                .andExpect(jsonPath("$.[1].name").value(studentTwo.getName()))
                .andExpect(jsonPath("$.[1].age").value(studentTwo.getAge()));
    }
}