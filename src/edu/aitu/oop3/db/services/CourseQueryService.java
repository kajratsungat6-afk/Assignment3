package edu.aitu.oop3.db.services;

import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.entities.CourseType;
import edu.aitu.oop3.db.repository.CourseRepository;
import edu.aitu.oop3.db.repository.EnrollmentRepository;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class CourseQueryService {

    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    public CourseQueryService(CourseRepository courseRepo, EnrollmentRepository enrollmentRepo) {
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public List<Course> findCourses(Predicate<Course> filter) {
        try {
            return courseRepo.findAll().stream()
                    .filter(filter) 
                    .sorted(Comparator.comparing(Course::getTitle)) 
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    public List<Course> availableCourses() {
        return findCourses(course -> {
            try {
                int enrolled = enrollmentRepo.countByCourseId(course.getId());
                return enrolled < course.getCapacity();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Course> availableCoursesByType(CourseType type) {
        return findCourses(course -> course.getType() == type);
    }
}
