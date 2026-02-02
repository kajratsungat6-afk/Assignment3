package edu.aitu.oop3.db.repository;

import edu.aitu.oop3.db.entities.Enrollment;

import java.sql.SQLException;
import java.util.List;

public interface EnrollmentRepository extends Repository<Enrollment, Integer> {
    int countByCourseId(int courseId) throws SQLException;
    void save(int studentId, int courseId) throws SQLException;

    List<Integer> findCourseIdsByStudentId(int studentId) throws SQLException;
}
