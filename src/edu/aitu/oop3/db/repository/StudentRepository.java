package edu.aitu.oop3.db.repository;

import edu.aitu.oop3.db.entities.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentRepository {
    void save(Student student) throws SQLException;
    List<Student> findAll() throws SQLException;
    Student findById(int id) throws SQLException;
}
