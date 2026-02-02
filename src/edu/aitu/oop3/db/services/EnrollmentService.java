package edu.aitu.oop3.db.services;

import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.entities.Student;
import edu.aitu.oop3.db.exceptions.CapacityExceededException;
import edu.aitu.oop3.db.exceptions.NotFoundException;
import edu.aitu.oop3.db.repository.CourseRepository;
import edu.aitu.oop3.db.repository.EnrollmentRepository;
import edu.aitu.oop3.db.repository.StudentRepository;

import java.sql.SQLException;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final StudentRepository studentRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo,
                             CourseRepository courseRepo,
                             StudentRepository studentRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
    }

    public void enrollStudent(int studentId, int courseId) {
        try {
            // 1) student exists?
            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

            // 2) course exists?
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

            // 3) capacity check
            int enrolledCount = enrollmentRepo.countByCourseId(courseId);
            if (enrolledCount >= course.getCapacity()) {
                throw new CapacityExceededException("Course capacity exceeded for course: " + courseId);
            }

            // 4) save enrollment
            enrollmentRepo.save(student.getId(), course.getId());

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }
}
