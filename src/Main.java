import edu.aitu.oop3.db.IDB.IDatabase;
import edu.aitu.oop3.db.config.DatabaseConnectionManager;
import edu.aitu.oop3.db.entities.Course;
import edu.aitu.oop3.db.entities.CourseType;
import edu.aitu.oop3.db.entities.Enrollment;
import edu.aitu.oop3.db.entities.Student;
import edu.aitu.oop3.db.entities.StudentSchedule;
import edu.aitu.oop3.db.factories.CourseFactory;
import edu.aitu.oop3.db.repository.CourseRepository;
import edu.aitu.oop3.db.repository.EnrollmentRepository;
import edu.aitu.oop3.db.repository.StudentRepository;
import edu.aitu.oop3.db.repositoryimpl.CourseRepositoryImpl;
import edu.aitu.oop3.db.repositoryimpl.EnrollmentRepositoryImpl;
import edu.aitu.oop3.db.repositoryimpl.StudentRepositoryImpl;
import edu.aitu.oop3.db.services.CourseQueryService;
import edu.aitu.oop3.db.services.EnrollmentService;
import edu.aitu.oop3.db.services.ScheduleService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        IDatabase db = DatabaseConnectionManager.getInstance();
        StudentRepository studentRepo = new StudentRepositoryImpl(db);
        CourseRepository courseRepo = new CourseRepositoryImpl(db);
        EnrollmentRepository enrollmentRepo = new EnrollmentRepositoryImpl(db);
        EnrollmentService enrollmentService =
                new EnrollmentService(enrollmentRepo, courseRepo, studentRepo);
        ScheduleService scheduleService =
                new ScheduleService(studentRepo, enrollmentRepo, courseRepo);
        CourseQueryService courseQueryService =
                new CourseQueryService(courseRepo, enrollmentRepo);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(scanner);
            try {
                switch (choice) {
                    case 1 -> createStudent(scanner, studentRepo);
                    case 2 -> listStudents(studentRepo);
                    case 3 -> findStudentById(scanner, studentRepo);
                    case 4 -> createCourse(scanner, courseRepo);
                    case 5 -> listCourses(courseRepo);
                    case 6 -> enrollStudent(scanner, enrollmentService);
                    case 7 -> listEnrollments(enrollmentRepo);
                    case 8 -> showStudentSchedule(scanner, scheduleService);
                    case 9 -> listAvailableCourses(courseQueryService);
                    case 10 -> listCoursesByType(scanner, courseQueryService);
                    case 0 -> {
                        running = false;
                        System.out.println("Bye!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
    private static void printMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Create student");
        System.out.println("2. List all students");
        System.out.println("3. Find student by id");
        System.out.println("4. Create course (Factory)");
        System.out.println("5. List courses");
        System.out.println("6. Register student to course");
        System.out.println("7. List enrollments");
        System.out.println("8. Show student schedule (Builder)");
        System.out.println("9. List available courses (Lambdas/Streams feature)");
        System.out.println("10. List courses by type (Lambda filter)");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }
    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }
    private static CourseType readCourseType(Scanner scanner) {
        System.out.println("Choose type: 1-LECTURE, 2-LAB, 3-ONLINE");
        int t = readInt(scanner);
        return switch (t) {
            case 2 -> CourseType.LAB;
            case 3 -> CourseType.ONLINE;
            default -> CourseType.LECTURE;
        };
    }
    private static void createStudent(Scanner scanner, StudentRepository studentRepo)
            throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        studentRepo.save(new Student(0, name, email));
        System.out.println("Student created.");
    }
    private static void listStudents(StudentRepository studentRepo) throws
            SQLException {
        List<Student> list = studentRepo.findAll();
        if (list.isEmpty()) {
            System.out.println("(No students)");
            return;
        }
        for (Student s : list) {
            System.out.println(s.getId() + " | " + s.getName() + " | " +
                    s.getEmail());
        }
    }
    private static void findStudentById(Scanner scanner, StudentRepository
            studentRepo) throws SQLException {
        System.out.print("Enter student id: ");
        int id = readInt(scanner);
        var sOpt = studentRepo.findById(id);
        if (sOpt.isEmpty()) {
            System.out.println("Not found");
            return;
        }
        Student s = sOpt.get();
        System.out.println(s.getId() + " | " + s.getName() + " | " + s.getEmail());
    }
    private static void createCourse(Scanner scanner, CourseRepository courseRepo)
            throws SQLException {
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int cap = readInt(scanner);
        CourseType type = readCourseType(scanner);
        Course course = CourseFactory.create(type, 0, title, cap);
        courseRepo.save(course);
        System.out.println("Course created.");
    }
    private static void listCourses(CourseRepository courseRepo) throws SQLException {
        List<Course> list = courseRepo.findAll();
        if (list.isEmpty()) {
            System.out.println("(No courses)");
            return;
        }
        for (Course c : list) {
            System.out.println(c.getId() + " | " + c.getTitle() + " | " + c.getType()
                    + " | cap=" + c.getCapacity());
        }
    }
    private static void enrollStudent(Scanner scanner, EnrollmentService
            enrollmentService) {
        System.out.print("Enter student id: ");
        int sid = readInt(scanner);
        System.out.print("Enter course id: ");
        int cid = readInt(scanner);
        enrollmentService.enrollStudent(sid, cid);
        System.out.println("Student enrolled.");
    }
    private static void listEnrollments(EnrollmentRepository enrollmentRepo) throws
            SQLException {
        List<Enrollment> list = enrollmentRepo.findAll();
        if (list.isEmpty()) {
            System.out.println("(No enrollments)");
            return;
        }
        for (Enrollment e : list) {
            System.out.println(e.getId() + " | student " + e.getStudentId() + " |
                    course " + e.getCourseId());
        }
    }
    private static void showStudentSchedule(Scanner scanner, ScheduleService
            scheduleService) {
        System.out.print("Enter student id: ");
        int sid = readInt(scanner);
        StudentSchedule schedule = scheduleService.buildScheduleForStudent(sid);
        System.out.println("Schedule for student " + schedule.getStudentId() + ":");
        if (schedule.getCourses().isEmpty()) {
            System.out.println("(No courses)");
        } else {
            for (Course c : schedule.getCourses()) {
                System.out.println(c.getId() + " | " + c.getTitle() + " | " +
                        c.getType());
            }
        }
    }
    private static void listAvailableCourses(CourseQueryService courseQueryService) {
        var list = courseQueryService.availableCourses();
        if (list.isEmpty()) {
            System.out.println("(No available courses)");
            return;
        }
        for (Course c : list) {
            System.out.println(c.getId() + " | " + c.getTitle() + " | " + c.getType()
                    + " | cap=" + c.getCapacity());
        }
    }
    private static void listCoursesByType(Scanner scanner, CourseQueryService
            courseQueryService) {
        CourseType type = readCourseType(scanner);
        var list = courseQueryService.findCourses(c -> c.getType() == type); // lambda
        if (list.isEmpty()) {
            System.out.println("(No courses for this type)");
            return;
        }
        for (Course c : list) {
            System.out.println(c.getId() + " | " + c.getTitle() + " | " + c.getType()
                    + " | cap=" + c.getCapacity());
        }
    }
}