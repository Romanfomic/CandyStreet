package com.example.jdbc.app;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Student {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDateTime birthdate;
    private String stgroup;
    private int number;
}
