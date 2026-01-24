import edu.aitu.oop3.db.IDB.IDatabase;
import edu.aitu.oop3.db.IDB.PostgresDatabase;
import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.entities.Enrollment;
import edu.aitu.oop3.db.entities.Student;
import edu.aitu.oop3.db.services.EnrollmentService;
import edu.aitu.oop3.db.repository.CourseRepository;
import edu.aitu.oop3.db.repository.EnrollmentRepository;
import edu.aitu.oop3.db.repository.StudentRepository;
import edu.aitu.oop3.db.repositoryimpl.CourseRepositoryImpl;
import edu.aitu.oop3.db.repositoryimpl.EnrollmentRepositoryImpl;
import edu.aitu.oop3.db.repositoryimpl.StudentRepositoryImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        IDatabase db = new PostgresDatabase();

        StudentRepository studentRepo = new StudentRepositoryImpl(db);
        CourseRepository courseRepo = new CourseRepositoryImpl(db);
        EnrollmentRepository enrollmentRepo = new EnrollmentRepositoryImpl(db);

        EnrollmentService enrollmentService = new EnrollmentService(enrollmentRepo, courseRepo);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Create student");
            System.out.println("2. List all students");
            System.out.println("3. Find student by id");
            System.out.println("4. Create course");
            System.out.println("5. List courses");
            System.out.println("6. Register student to course");
            System.out.println("7. List enrollments");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number!");
                continue;
            }

            try {
                switch (choice) {

                    case 1 -> {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        studentRepo.save(new Student(0, name, email));
                        System.out.println("Student created.");
                    }

                    case 2 -> {
                        List<Student> list = studentRepo.findAll();
                        for (Student s : list) {
                            System.out.println(
                                    s.getId() + " | " + s.getName() + " | " + s.getEmail()
                            );
                        }
                    }

                    case 3 -> {
                        System.out.print("Enter student id: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        Student s = studentRepo.findById(id);
                        System.out.println(s != null ? s : "Not found");
                    }

                    case 4 -> {
                        System.out.print("Enter course name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter capacity: ");
                        int cap = Integer.parseInt(scanner.nextLine());
                        courseRepo.save(new Course(0, name, cap));
                        System.out.println("Course created.");
                    }

                    case 5 -> {
                        List<Course> list = courseRepo.findAll();
                        for (Course c : list) {
                            System.out.println(
                                    c.getId() + " | " + c.getTitle() + " | " + c.getCapacity()
                            );
                        }
                    }

                    case 6 -> {
                        System.out.print("Enter student id: ");
                        int sid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter course id: ");
                        int cid = Integer.parseInt(scanner.nextLine());

                        enrollmentService.enrollStudent(sid, cid);
                        System.out.println("Student enrolled.");
                    }

                    case 7 -> {
                        List<Enrollment> list = enrollmentRepo.findAll();
                        for (Enrollment e : list) {
                            System.out.println(
                                    e.getId() + " | student " + e.getStudentId() +
                                            " | course " + e.getCourseId()
                            );
                        }
                    }

                    case 0 -> {
                        running = false;
                        System.out.println("Bye!");
                    }

                    default -> System.out.println("Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
