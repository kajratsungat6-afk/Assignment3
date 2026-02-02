package edu.aitu.oop3.db.entities;

public class LectureCourse extends Course {
    public LectureCourse(int id, String title, int capacity) {
        super(id, title, capacity);
    }

    @Override
    public CourseType getType() {
        return CourseType.LECTURE;
    }
}
