package com.example.jdbc.app;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
