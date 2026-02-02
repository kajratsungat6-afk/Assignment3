package edu.aitu.oop3.db.entities;

public class OnlineCourse extends Course {
    public OnlineCourse(int id, String title, int capacity) {
        super(id, title, capacity);
    }

    @Override
    public CourseType getType() {
        return CourseType.ONLINE;
    }
}
