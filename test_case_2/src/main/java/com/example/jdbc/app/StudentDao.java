package com.example.jdbc.app;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StudentDao {
    private final NamedParameterJdbcTemplate template;

    public StudentDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public int createStudent(Student student) {
        String sql = "INSERT INTO student (name, surname, patronymic, birthdate, stgroup, number)" +
                "VALUES (:name, :surname, :patronymic, :birthdate, :stgroup, :number) RETURNING ID";
        Map<String, Object> map = new HashMap<>();
        map.put("name", student.getName());
        map.put("surname", student.getSurname());
        map.put("patronymic", student.getPatronymic());
        map.put("birthdate", student.getBirthdate());
        map.put("stgroup", student.getStgroup());
        map.put("number", student.getNumber());

        return  template.queryForObject(sql, map, int.class);
    }
}
