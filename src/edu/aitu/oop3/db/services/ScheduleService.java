package edu.aitu.oop3.db.services;

import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.entities.StudentSchedule;
import edu.aitu.oop3.db.exceptions.NotFoundException;
import edu.aitu.oop3.db.repository.CourseRepository;
import edu.aitu.oop3.db.repository.EnrollmentRepository;
import edu.aitu.oop3.db.repository.StudentRepository;

import java.sql.SQLException;
import java.util.List;

public class ScheduleService {

    private final StudentRepository studentRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;

    public ScheduleService(StudentRepository studentRepo,
                           EnrollmentRepository enrollmentRepo,
                           CourseRepository courseRepo) {
        this.studentRepo = studentRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.courseRepo = courseRepo;
    }

    public StudentSchedule buildScheduleForStudent(int studentId) {
        try {
            // 1) student must exist
            studentRepo.findById(studentId)
                    .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

            // 2) find course ids
            List<Integer> courseIds = enrollmentRepo.findCourseIdsByStudentId(studentId);

            // 3) build schedule using Builder
            StudentSchedule.Builder builder = new StudentSchedule.Builder(studentId);

            for (Integer courseId : courseIds) {
                courseRepo.findById(courseId).ifPresent(builder::addCourse);
            }

            return builder.build();

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }
}
