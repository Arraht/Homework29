package ru.hogwarts.school.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Objects;

@DynamicInsert
@Entity
public class Student {
    @Id
    @GeneratedValue()
    private Long id;
    private String name;
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Student(Long id, String name, Integer age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setStudent(Long id, String name, Integer age) {
        setId(id);
        setName(name);
        setAge(age);
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Student student = (Student) object;
        return age == student.age && Objects.equals(id, student.id) && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @Override
    public String toString() {
        return "name " + this.name + " " + this.age + " " + this.id;
    }
}