package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.interfaces.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentService studentService;
    private ResponseEntity<Faculty> newFaculty;
    private ResponseEntity<Faculty> newFacultyTwo;
    private ResponseEntity<Faculty> newFacultyThree;
    private ResponseEntity<Student> newStudent;
    private ResponseEntity<Student> newStudentTwo;
    private ResponseEntity<Student> newStudentThree;

    @BeforeEach
    public void createSetUp() {
        newFaculty = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(1L, "name faculty", "color"), Faculty.class);
        newStudent = testRestTemplate.postForEntity("http://localhost:" + port
                + "/student", new Student(1L, "new Student", 12, newFaculty.getBody()), Student.class);
        newFacultyTwo = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(2L, "name facultyTwo", "colorTwo"), Faculty.class);
        newStudentTwo = testRestTemplate.postForEntity("http://localhost:" + port
                + "/student", new Student(2L, "new StudentTwo", 15, newFacultyTwo.getBody()), Student.class);
        newFacultyThree = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(3L, "name facultyThree", "colorThree"), Faculty.class);
        newStudentThree = testRestTemplate.postForEntity("http://localhost:" + port
                + "/student", new Student(3L, "new StudentThree", 11,
                newFacultyThree.getBody()), Student.class);
        assertThat(newFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newFacultyTwo.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newStudentTwo.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newStudentThree.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newFacultyThree.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @AfterEach
    public void deleteSetUp() {
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + newStudent.getBody().getId(), newStudent);
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + newStudentTwo.getBody().getId(), newStudentTwo);
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + newStudentThree.getBody().getId(), newStudentThree);
    }

    @Test
    public void createStudentTest() {
        assertThat(newStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newStudent.getBody().getName()).isEqualTo("new Student");
        assertThat(newStudent.getBody().getAge()).isEqualTo(12);
        assertThat(newStudent.getBody().getFaculty().getName()).isEqualTo("name faculty");
        assertThat(newStudent.getBody().getFaculty().getColor()).isEqualTo("color");
    }

    @Test
    public void getStudentTest() {
        ResponseEntity<Student> studentResponse = testRestTemplate.getForEntity("http://localhost:" + port
                + "/student/" + newStudent.getBody().getId(), Student.class);
        assertThat(studentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponse.getBody()).isNotNull();
        assertThat(studentResponse.getBody().getName()).isEqualTo("new Student");
        assertThat(studentResponse.getBody().getAge()).isEqualTo(12);
        assertThat(newStudent.getBody().getFaculty().getName()).isEqualTo("name faculty");
        assertThat(newStudent.getBody().getFaculty().getColor()).isEqualTo("color");
    }

    @Test
    public void editStudentTest() {
        testRestTemplate.put("http://localhost:" + port
                + "/student", new Student(Objects.requireNonNull(newStudent.getBody()).getId(),
                "test Student", 15));
        ResponseEntity<Student> studentResponse = testRestTemplate.getForEntity("http://localhost:" + port
                + "/student/" + newStudent.getBody().getId(), Student.class);
        assertThat(Objects.requireNonNull(studentResponse.getBody()).getName()).isEqualTo("test Student");
        assertThat(studentResponse.getBody().getAge()).isEqualTo(15);
        assertThat(studentResponse.getBody().getFaculty().getName()).isEqualTo("name faculty");
        assertThat(studentResponse.getBody().getFaculty().getColor()).isEqualTo("color");
    }

    @Test
    public void removeStudentTest() {
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + newStudent.getBody().getId(), newStudent);
        ResponseEntity<Student> getStudent = testRestTemplate.getForEntity("http://localhost:" + port
                + "/student/" + newStudent.getBody().getId(), Student.class);
        assertThat(getStudent.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getStudentByAgeTest() {
        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port
                + "/student/age").queryParam("age", 12).toUriString();
        ResponseEntity<List<Student>> getStudent = testRestTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });
        assertThat(getStudent.getBody().size()).isEqualTo(1);
        assertThat(getStudent.getBody().get(0).getName()).isEqualTo("new Student");
        assertThat(getStudent.getBody().get(0).getAge()).isEqualTo(12);
    }

    @Test
    public void getStudentByAgeBetweenTest() {
        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port
                        + "/student/age/between")
                .queryParam("minAge", 11)
                .queryParam("maxAge", 12).toUriString();
        ResponseEntity<Collection<Student>> getStudent = testRestTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });
        assertThat(getStudent.getBody().size()).isEqualTo(2);
        assertThat(getStudent.getBody().stream().toList().get(0).getName()).isEqualTo("new Student");
        assertThat(getStudent.getBody().stream().toList().get(0).getAge()).isEqualTo(12);
        assertThat(getStudent.getBody().stream().toList().get(0).getFaculty().getName()).isEqualTo("name faculty");
        assertThat(getStudent.getBody().stream().toList().get(0).getFaculty().getColor()).isEqualTo("color");
        assertThat(getStudent.getBody().stream().toList().get(1).getName()).isEqualTo("new StudentThree");
        assertThat(getStudent.getBody().stream().toList().get(1).getAge()).isEqualTo(11);
        assertThat(getStudent.getBody().stream().toList().get(1).getFaculty().getName())
                .isEqualTo("name facultyThree");
        assertThat(getStudent.getBody().stream().toList().get(1).getFaculty()
                .getColor()).isEqualTo("colorthree");
    }

    @Test
    public void findFacultyFromStudentTest() {
        ResponseEntity<Faculty> getFacultyByStudent = testRestTemplate.getForEntity("http://localhost:" + port
                + "/student/" + newStudent.getBody().getId() + "/faculty", Faculty.class);
        assertThat(getFacultyByStudent.getBody().getName()).isEqualTo("name faculty");
        assertThat(getFacultyByStudent.getBody().getColor()).isEqualTo("color");
    }
}