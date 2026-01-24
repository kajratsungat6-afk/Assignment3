package edu.aitu.oop3.db.repository;


import edu.aitu.oop3.db.entities.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseRepository {
    void save(Course course) throws SQLException;
    Course findById(int id) throws SQLException;
    List<Course> findAll() throws SQLException; // ← ОБЯЗАТЕЛЬНО

}
