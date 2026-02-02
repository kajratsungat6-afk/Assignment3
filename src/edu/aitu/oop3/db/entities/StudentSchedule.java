package edu.aitu.oop3.db.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentSchedule {
    private final int studentId;
    private final List<Course> courses;

    private StudentSchedule(Builder builder) {
        this.studentId = builder.studentId;
        this.courses = List.copyOf(builder.courses);
    }

    public int getStudentId() {
        return studentId;
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    // ===== Builder =====
    public static class Builder {
        private final int studentId;
        private final List<Course> courses = new ArrayList<>();

        public Builder(int studentId) {
            this.studentId = studentId;
        }

        public Builder addCourse(Course course) {
            courses.add(course);
            return this;
        }

        public Builder addCourses(List<Course> list) {
            courses.addAll(list);
            return this;
        }

        public StudentSchedule build() {
            return new StudentSchedule(this);
        }
    }
}
