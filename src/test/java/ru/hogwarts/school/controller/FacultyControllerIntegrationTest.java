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
import ru.hogwarts.school.service.interfaces.FacultyService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FacultyService facultyService;
    private ResponseEntity<Faculty> newFaculty;
    private ResponseEntity<Faculty> newFacultyTwo;
    private ResponseEntity<Faculty> newFacultyThree;
    ResponseEntity<Faculty> newFacultyFour;
    private ResponseEntity<Student> newStudent;
    private ResponseEntity<Student> newStudentTwo;

    @BeforeEach
    public void createSetUp() {
        newFaculty = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(1L, "name faculty", "color"), Faculty.class);
        newFacultyTwo = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(2L, "name facultyTwo", "colorTwo"), Faculty.class);
        newFacultyThree = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(3L, "name facultyThree", "colorThree"), Faculty.class);
        newFacultyFour = testRestTemplate.postForEntity("http://localhost:" + port
                + "/faculty", new Faculty(4L, "name facultyFour", "color"), Faculty.class);
        newStudent = testRestTemplate.postForEntity("http://localhost:" + port
                + "/student", new Student(1L, "new Student", 12, newFaculty.getBody()), Student.class);
        newStudentTwo = testRestTemplate.postForEntity("http://localhost:" + port
                + "/student", new Student(2L, "new StudentTwo", 15, newFaculty.getBody()), Student.class);
    }

    @AfterEach
    public void deleteSetUp() {
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + Objects.requireNonNull(newStudent.getBody()).getId(), newStudent);
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + Objects.requireNonNull(newStudentTwo.getBody()).getId(), newStudentTwo);
        testRestTemplate.delete("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFaculty.getBody()).getId(), newFaculty);
        testRestTemplate.delete("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFacultyTwo.getBody()).getId(), newFacultyTwo);
        testRestTemplate.delete("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFacultyThree.getBody()).getId(), newFacultyThree);
        testRestTemplate.delete("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFacultyFour.getBody()).getId(), newFacultyFour);
    }

    @Test
    public void createFacultyTest() {
        assertThat(newFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newFaculty.getBody()).getName()).isEqualTo("name faculty");
        assertThat(newFaculty.getBody().getColor()).isEqualTo("color");
    }

    @Test
    public void addStudentByFacultyTest() {
        ResponseEntity<Student> newStudentTest = testRestTemplate.postForEntity("http://localhost:" + port
                + "/student", new Student(2L, "newStudentTest", 12), Student.class);
        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port
                        + "/faculty/add/student").queryParam("studentId", 2)
                .queryParam("facultyId", 1).toUriString();
        ResponseEntity<Student> studentResponse = testRestTemplate.postForEntity(uri, newStudentTest, Student.class);
        assertThat(Objects.requireNonNull(studentResponse.getBody()).getFaculty().getId()).isEqualTo(1);
        assertThat(studentResponse.getBody().getFaculty().getName()).isEqualTo("name faculty");
        assertThat(studentResponse.getBody().getFaculty().getColor()).isEqualTo("color");
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + Objects.requireNonNull(newStudentTest.getBody()).getId(), newStudentTest);
    }

    @Test
    public void removeFacultyTest() {
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + Objects.requireNonNull(newStudentTwo.getBody()).getId(), newStudentTwo);
        testRestTemplate.delete("http://localhost:" + port
                + "/student/" + Objects.requireNonNull(newStudent.getBody()).getId(), newStudent);
        testRestTemplate.delete("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFaculty.getBody()).getId(), newFaculty);
        ResponseEntity<Faculty> getFaculty = testRestTemplate.getForEntity("http://localhost:" + port
                + "/faculty/" + newFaculty.getBody().getId(), Faculty.class);
        assertThat(getFaculty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getFacultyTest() {
        ResponseEntity<Faculty> getFaculty = testRestTemplate.getForEntity("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFaculty.getBody()).getId(), Faculty.class);
        assertThat(Objects.requireNonNull(getFaculty.getBody()).getName()).isEqualTo("name faculty");
        assertThat(getFaculty.getBody().getColor()).isEqualTo("color");
    }

    @Test
    public void getFacultyByColor() {
        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port
                + "/faculty/color").queryParam("color", "color").toUriString();
        ResponseEntity<List<Faculty>> getFaculty = testRestTemplate.exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Faculty>>() {
                });
        assertThat(Objects.requireNonNull(getFaculty.getBody()).size()).isEqualTo(2);
        assertThat(getFaculty.getBody().get(0).getName()).isEqualTo("name faculty");
        assertThat(getFaculty.getBody().get(0).getColor()).isEqualTo("color");
        assertThat(getFaculty.getBody().get(1).getName()).isEqualTo("name facultyFour");
        assertThat(getFaculty.getBody().get(1).getColor()).isEqualTo("color");
    }

    @Test
    public void getFacultyByNameOrColor() {
        ResponseEntity<Collection<Faculty>> getFacultyByName = testRestTemplate
                .exchange("http://localhost:" + port + "/faculty/find?name=name faculty", HttpMethod.GET,
                        null, new ParameterizedTypeReference<Collection<Faculty>>() {
                        });
        ResponseEntity<Collection<Faculty>> getFacultyByColor = testRestTemplate
                .exchange("http://localhost:" + port + "/faculty/find?color=color", HttpMethod.GET,
                        null, new ParameterizedTypeReference<Collection<Faculty>>() {
                        });
        assertThat(Objects.requireNonNull(getFacultyByName.getBody()).size()).isEqualTo(1);
        assertThat(getFacultyByName.getBody().stream().toList().getFirst().getName()).isEqualTo("name faculty");
        assertThat(Objects.requireNonNull(getFacultyByColor.getBody()).size()).isEqualTo(2);
        assertThat(getFacultyByColor.getBody().stream().toList().get(0).getName()).isEqualTo("name faculty");
        assertThat(getFacultyByColor.getBody().stream().toList().get(0).getColor()).isEqualTo("color");
        assertThat(getFacultyByColor.getBody().stream().toList().get(1).getName()).isEqualTo("name facultyFour");
        assertThat(getFacultyByColor.getBody().stream().toList().get(1).getColor()).isEqualTo("color");
    }

    @Test
    public void editFacultyTest() {
        testRestTemplate.put("http://localhost:" + port + "/faculty", new Faculty(newFaculty.getBody().getId(),
                "test faculty", "test color"));
        ResponseEntity<Faculty> getFaculty = testRestTemplate.getForEntity("http://localhost:" + port
                + "/faculty/" + newFaculty.getBody().getId(), Faculty.class);
        assertThat(Objects.requireNonNull(getFaculty.getBody()).getName()).isEqualTo("test faculty");
        assertThat(getFaculty.getBody().getColor()).isEqualTo("test color");
    }

    @Test
    public void findStudentFromFacultyTest() {
        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port
                + "/faculty/" + Objects.requireNonNull(newFaculty.getBody()).getId() + "/students").toUriString();
        ResponseEntity<List<Student>> getStudentByFaculty = testRestTemplate.exchange(uri, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Student>>() {
                });
        assertThat(Objects.requireNonNull(getStudentByFaculty.getBody()).size()).isEqualTo(2);
        assertThat(getStudentByFaculty.getBody().getFirst().getName()).isEqualTo("new Student");
        assertThat(getStudentByFaculty.getBody().getFirst().getAge()).isEqualTo(12);
        assertThat(getStudentByFaculty.getBody().getFirst().getFaculty().getName()).isEqualTo("name faculty");
        assertThat(getStudentByFaculty.getBody().getFirst().getFaculty().getColor()).isEqualTo("color");
        assertThat(getStudentByFaculty.getBody().get(1).getName()).isEqualTo("new StudentTwo");
        assertThat(getStudentByFaculty.getBody().get(1).getAge()).isEqualTo(15);
        assertThat(getStudentByFaculty.getBody().get(1).getFaculty().getName()).isEqualTo("name faculty");
        assertThat(getStudentByFaculty.getBody().get(1).getFaculty().getColor()).isEqualTo("color");
    }
}