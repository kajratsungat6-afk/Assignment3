package edu.aitu.oop3.db.repositoryimpl;

import edu.aitu.oop3.db.IDB.IDatabase;
import edu.aitu.oop3.db.entities.Student;
import edu.aitu.oop3.db.repository.StudentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepositoryImpl implements StudentRepository {
    private final IDatabase db;

    public StudentRepositoryImpl(IDatabase db) {
        this.db = db;
    }

    @Override
    public void save(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, email) VALUES (?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Student> findById(Integer id) throws SQLException {
        String sql = "SELECT id, name, email FROM students WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                return Optional.of(s);
            }
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, email FROM students";

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        }
        return list;
    }
}
