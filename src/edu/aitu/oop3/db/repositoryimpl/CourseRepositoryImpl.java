package edu.aitu.oop3.db.repositoryimpl;

import edu.aitu.oop3.db.IDB.IDatabase;
import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.entities.CourseType;
import edu.aitu.oop3.db.factories.CourseFactory;
import edu.aitu.oop3.db.repository.CourseRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepositoryImpl implements CourseRepository {

    private final IDatabase db;

    public CourseRepositoryImpl(IDatabase db) {
        this.db = db;
    }

    @Override
    public void save(Course course) throws SQLException {
        String sql = "INSERT INTO courses(title, capacity, type) VALUES (?, ?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setInt(2, course.getCapacity());
            stmt.setString(3, course.getType().name());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Course> findById(Integer id) throws SQLException {
        String sql = "SELECT id, title, capacity, type FROM courses WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                CourseType type = CourseType.valueOf(rs.getString("type"));
                Course course = CourseFactory.create(
                        type,
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("capacity")
                );
                return Optional.of(course);
            }
        }
    }

    @Override
    public List<Course> findAll() throws SQLException {
        String sql = "SELECT id, title, capacity, type FROM courses";
        List<Course> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CourseType type = CourseType.valueOf(rs.getString("type"));
                list.add(CourseFactory.create(
                        type,
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("capacity")
                ));
            }
        }
        return list;
    }
}
