package edu.aitu.oop3.db.factories;

import edu.aitu.oop3.db.entities.*;

public final class CourseFactory {
    private CourseFactory() {}

    public static Course create(CourseType type, int id, String title, int capacity) {
        return switch (type) {
            case LECTURE -> new LectureCourse(id, title, capacity);
            case LAB -> new LabCourse(id, title, capacity);
            case ONLINE -> new OnlineCourse(id, title, capacity);
        };
    }
}
