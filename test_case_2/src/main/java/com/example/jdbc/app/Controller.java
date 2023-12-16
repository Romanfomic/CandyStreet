package com.example.jdbc.app;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    private final StudentDao dao;

    public Controller(StudentDao dao) {
        this.dao = dao;
    }

    @CrossOrigin
    @PostMapping("/createStudent")
    public int createStudent(@RequestBody Student student) {
        return dao.createStudent(student);
    }

    @CrossOrigin
    @GetMapping("/getStudent")
    public Student getStudentById(@RequestParam int id) {
        return dao.getStudentById(id);
    }

    @CrossOrigin
    @GetMapping("/getStudents")
    public List<Student> getStudents() {
        return dao.getStudents();
    }

    @CrossOrigin
    @DeleteMapping("/deleteStudent")
    public void deleteStudentById(@RequestParam int id) {
        dao.deleteStudentById(id);
    }
}
