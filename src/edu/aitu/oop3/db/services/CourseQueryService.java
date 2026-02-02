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

    // универсальный метод: принимает лямбду-фильтр
    public List<Course> findCourses(Predicate<Course> filter) {
        try {
            return courseRepo.findAll().stream()
                    .filter(filter) // <-- lambda / functional interface (Predicate)
                    .sorted(Comparator.comparing(Course::getTitle)) // <-- method reference
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    // новая фича: доступные курсы (есть свободные места)
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

    // доступные курсы только выбранного типа (LECTURE/LAB/ONLINE)
    public List<Course> availableCoursesByType(CourseType type) {
        return findCourses(course -> course.getType() == type);
    }
}
