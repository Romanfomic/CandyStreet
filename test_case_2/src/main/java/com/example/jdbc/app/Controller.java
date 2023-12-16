package com.example.jdbc.app;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    private final StudentDao dao;

    public Controller(StudentDao dao) {
        this.dao = dao;
    }

    @PostMapping
    public int createStudent(@RequestBody Student student) {
        return dao.createStudent(student);
    }
    @GetMapping
    public Student getStudentById(@RequestParam int id) {
        return dao.getStudentById(id);
    }

    @GetMapping("/all")
    public List<Student> getStudents() {
        return dao.getStudents();
    }

    @DeleteMapping
    public void deleteStudentById(@RequestParam int id) {
        dao.deleteStudentById(id);
    }
}
