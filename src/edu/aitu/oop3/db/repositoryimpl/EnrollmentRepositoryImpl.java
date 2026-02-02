package edu.aitu.oop3.db.repositoryimpl;

import edu.aitu.oop3.db.IDB.IDatabase;
import edu.aitu.oop3.db.entities.Enrollment;
import edu.aitu.oop3.db.repository.EnrollmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final IDatabase db;

    public EnrollmentRepositoryImpl(IDatabase db) {
        this.db = db;
    }

    // ====== Generic Repository methods ======

    @Override
    public void save(Enrollment entity) throws SQLException {
        // поддержим общий save(entity)
        String sql = "INSERT INTO enrollments(student_id, course_id) VALUES (?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, entity.getStudentId());
            stmt.setInt(2, entity.getCourseId());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Enrollment> findById(Integer id) throws SQLException {
        String sql = "SELECT id, student_id, course_id FROM enrollments WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Enrollment e = new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id")
                );
                return Optional.of(e);
            }
        }
    }

    @Override
    public List<Enrollment> findAll() throws SQLException {
        String sql = "SELECT id, student_id, course_id FROM enrollments";
        List<Enrollment> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id")
                ));
            }
        }
        return list;
    }

    // ====== Your custom methods ======

    @Override
    public int countByCourseId(int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public void save(int studentId, int courseId) throws SQLException {
        // оставляем твой удобный метод (просто вызывает общий save)
        save(new Enrollment(0, studentId, courseId));
    }

    @Override
    public List<Integer> findCourseIdsByStudentId(int studentId) throws SQLException {
        String sql = "SELECT course_id FROM enrollments WHERE student_id = ?";
        List<Integer> ids = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("course_id"));
                }
            }
        }

        return ids;
    }

}
