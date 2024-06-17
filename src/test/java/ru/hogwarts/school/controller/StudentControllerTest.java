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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class StudentControllerTest {
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
    private StudentController studentController;
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
    public void createStudentTest() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(studentName))
                .andExpect(jsonPath("$.age").value(studentAge))
                .andExpect(jsonPath("$.faculty.id").value(facultyId))
                .andExpect(jsonPath("$.faculty.name").value(facultyName))
                .andExpect(jsonPath("$.faculty.color").value(facultyColor));
    }

    @Test
    public void getStudentTest() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/12")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(studentName))
                .andExpect(jsonPath("$.age").value(studentAge))
                .andExpect(jsonPath("$.faculty.id").value(facultyId))
                .andExpect(jsonPath("$.faculty.name").value(facultyName))
                .andExpect(jsonPath("$.faculty.color").value(facultyColor));
    }

    @Test
    public void editStudentTest() throws Exception {
        Student studentTest = new Student(2L, "Гарри Поттер", 11);
        studentTest.setFaculty(faculty);
        when(studentRepository.save(any(Student.class))).thenReturn(studentTest);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(studentTest.getId()))
                .andExpect(jsonPath("$.name").value(studentTest.getName()))
                .andExpect(jsonPath("$.age").value(studentTest.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(facultyId))
                .andExpect(jsonPath("$.faculty.name").value(facultyName))
                .andExpect(jsonPath("$.faculty.color").value(facultyColor));
    }

    @Test
    public void removeStudentTest() throws Exception {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(studentName))
                .andExpect(jsonPath("$.age").value(studentAge))
                .andExpect(jsonPath("$.faculty.id").value(facultyId))
                .andExpect(jsonPath("$.faculty.name").value(facultyName))
                .andExpect(jsonPath("$.faculty.color").value(facultyColor));
    }

    @Test
    public void getStudentByAge() throws Exception {
        Student studentTest = new Student(2L, "Гарри Поттер", 11);
        studentTest.setFaculty(faculty);
        List<Student> studentList = new ArrayList<>(List.of(studentTest, student));
        when(studentRepository.findAll()).thenReturn(studentList);
        mockMvc.perform(MockMvcRequestBuilders.get("/student/age?age=11")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(studentTest.getId()))
                .andExpect(jsonPath("$.[0].name").value(studentTest.getName()))
                .andExpect(jsonPath("$.[0].age").value(studentTest.getAge()))
                .andExpect(jsonPath("$.[0].faculty.id").value(facultyId))
                .andExpect(jsonPath("$.[0].faculty.name").value(facultyName))
                .andExpect(jsonPath("$.[0].faculty.color").value(facultyColor));
    }

    @Test
    public void getStudentByAgeBetweenTest() throws Exception {
        Student studentOne = new Student(2L, "Гарри Поттер", 11);
        Student studentTwo = new Student(1L, "Рон Уизли", 17);
        student.setAge(11);
        studentOne.setFaculty(faculty);
        List<Student> studentList = new ArrayList<>(List.of(studentOne, student, studentTwo));
        List<Student> students = studentList
                .stream()
                .filter(student1 -> student1.getAge() == 11)
                .toList();
        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/student/age/between?minAge=0&maxAge=20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(studentOne.getId()))
                .andExpect(jsonPath("$.[0].name").value(studentOne.getName()))
                .andExpect(jsonPath("$.[0].age").value(studentOne.getAge()))
                .andExpect(jsonPath("$.[0].faculty.id").value(facultyId))
                .andExpect(jsonPath("$.[0].faculty.name").value(facultyName))
                .andExpect(jsonPath("$.[0].faculty.color").value(facultyColor))
                .andExpect(jsonPath("$.[1].id").value(student.getId()))
                .andExpect(jsonPath("$.[1].name").value(student.getName()))
                .andExpect(jsonPath("$.[1].age").value(student.getAge()))
                .andExpect(jsonPath("$.[1].faculty.id").value(facultyId))
                .andExpect(jsonPath("$.[1].faculty.name").value(facultyName))
                .andExpect(jsonPath("$.[1].faculty.color").value(facultyColor));
    }

    @Test
    public void findFacultyFromStudentTest() throws Exception {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/2/faculty")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }
}