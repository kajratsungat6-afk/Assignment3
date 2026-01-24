package edu.aitu.oop3.db.services;


import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.exceptions.CapacityExceededException;
import edu.aitu.oop3.db.exceptions.NotFoundException;
import edu.aitu.oop3.db.repository.CourseRepository;
import edu.aitu.oop3.db.repository.EnrollmentRepository;

import java.sql.SQLException;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public void enrollStudent(int studentId, int courseId) {

        try {
            // check that course exists
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                throw new NotFoundException("Course not found");
            }

            // how many students enrolled
            int enrolledCount = enrollmentRepository.countByCourseId(courseId);

            // capacity check
            if (enrolledCount >= course.getCapacity()) {
                throw new CapacityExceededException(
                        "Course capacity exceeded"
                );
            }

            // student enrolling
            enrollmentRepository.save(studentId, courseId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
