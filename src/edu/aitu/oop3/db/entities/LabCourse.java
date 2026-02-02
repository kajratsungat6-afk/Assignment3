package edu.aitu.oop3.db.entities;

public class LabCourse extends Course {
    public LabCourse(int id, String title, int capacity) {
        super(id, title, capacity);
    }

    @Override
    public CourseType getType() {
        return CourseType.LAB;
    }
}
