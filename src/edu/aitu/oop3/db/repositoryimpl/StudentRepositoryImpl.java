package edu.aitu.oop3.db.repositoryimpl;

import edu.aitu.oop3.db.IDB.IDatabase;
import edu.aitu.oop3.db.entities.Student;
import edu.aitu.oop3.db.repository.StudentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    private final IDatabase db;

    public StudentRepositoryImpl(IDatabase db) {
        this.db = db;
    }

    @Override
    public void save(Student student) throws SQLException {
        String sql = "insert into students(name, email) values (?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "select id, name, email from students";
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

    @Override
    public Student findById(int id) throws SQLException {
        String sql = "select id, name, email from students where id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            } else {
                throw new RuntimeException("Student not found");
            }
        }
    }
}

