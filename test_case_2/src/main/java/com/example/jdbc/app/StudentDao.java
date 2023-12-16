package com.example.jdbc.app;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDao {
    private final NamedParameterJdbcTemplate template;

    public StudentDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public int createStudent(Student student) {
        String sql = "INSERT INTO student (name, surname, patronymic, birthdate, stgroup, number)" +
                "VALUES (:name, :surname, :patronymic, :birthdate, :stgroup, :number) RETURNING ID";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", student.getName())
                .addValue("surname", student.getSurname())
                .addValue("patronymic", student.getPatronymic())
                .addValue("birthdate", student.getBirthdate())
                .addValue("stgroup", student.getStgroup())
                .addValue("number", student.getNumber());

        return  template.queryForObject(sql, parameterSource, int.class);
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM student WHERE student.id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        return template.queryForObject(sql, parameterSource, ((rs, rowNum) -> {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setSurname(rs.getString("surname"));
            student.setPatronymic(rs.getString("patronymic"));
            student.setBirthdate(rs.getTimestamp("birthdate").toLocalDateTime().toLocalDate());
            student.setStgroup(rs.getString("stgroup"));
            student.setNumber(rs.getInt("number"));
            return student;
        }));
    }

    public List<Student> getStudents() {
        String sql = "SELECT * FROM student";
        SqlParameterSource parameterSource = new MapSqlParameterSource();
        return template.queryForObject(sql, parameterSource, ((rs, rowNum) -> {
            List<Student> students = new ArrayList<>();
            do {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setSurname(rs.getString("surname"));
                student.setPatronymic(rs.getString("patronymic"));
                student.setBirthdate(rs.getTimestamp("birthdate").toLocalDateTime().toLocalDate());
                student.setStgroup(rs.getString("stgroup"));
                student.setNumber(rs.getInt("number"));
                students.add(student);
            } while (rs.next());
            return students;
        }));
    }

    public void deleteStudentById(int id) {
        String sql = "DELETE FROM student WHERE student.id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        template.update(sql, parameterSource);
    }
}
