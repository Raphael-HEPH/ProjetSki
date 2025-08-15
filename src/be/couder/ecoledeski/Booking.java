package be.couder.ecoledeski;

import java.util.List;

import be.couder.ecoleski.DAO_Folder.BookingDAO;
import be.couder.ecoleski.DAO_Folder.InstructorDAO;
import be.couder.ecoleski.DAO_Folder.LessonDAO;
import be.couder.ecoleski.DAO_Folder.PeriodDAO;
import be.couder.ecoleski.DAO_Folder.StudentDAO;

public class Booking {

    private int id;
    private boolean hasInsurance;
    private boolean hasDiscount;
    private Lesson lesson;
    private Instructor instructor;
    private Student student;
    private Period period;

    private static final BookingDAO bookingDAO = new BookingDAO();
    private static final LessonDAO lessonDAO = new LessonDAO();
    private static final InstructorDAO instructorDAO = new InstructorDAO();
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final PeriodDAO periodDAO = new PeriodDAO();

    public Booking(int id, boolean hasInsurance, boolean hasDiscount, Lesson lesson, Instructor instructor,
                   Student student, Period period) {
        this.id = id;
        this.hasInsurance = hasInsurance;
        this.hasDiscount = hasDiscount;
        this.lesson = lesson;
        this.instructor = instructor;
        this.student = student;
        this.period = period;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public boolean isHasInsurance() { return hasInsurance; }
    public void setHasInsurance(boolean hasInsurance) { this.hasInsurance = hasInsurance; }

    public boolean isHasDiscount() { return hasDiscount; }
    public void setHasDiscount(boolean hasDiscount) { this.hasDiscount = hasDiscount; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Period getPeriod() { return period; }
    public void setPeriod(Period period) { this.period = period; }

    public void chargerRelations() {
        if (this.getId() <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");

        if (this.lesson != null && this.lesson.getId() > 0) {
            Lesson fullLesson = lessonDAO.getById(this.lesson.getId());
            if (fullLesson != null) {
                this.setLesson(fullLesson);
                fullLesson.addBooking(this);
            }
        }

        if (this.instructor != null && this.instructor.getId() > 0) {
            Instructor fullInstructor = instructorDAO.getById(this.instructor.getId());
            if (fullInstructor != null) {
                this.setInstructor(fullInstructor);
                fullInstructor.addBooking(this);
            }
        }

        if (this.student != null && this.student.getId() > 0) {
            Student fullStudent = studentDAO.GetById(this.student.getId());
            if (fullStudent != null) {
                this.setStudent(fullStudent);
                fullStudent.addBooking(this);
            }
        }

        if (this.period != null && this.period.getId() > 0) {
            Period fullPeriod = periodDAO.getById(this.period.getId());
            if (fullPeriod != null) {
                this.setPeriod(fullPeriod);
                fullPeriod.addBooking(this);
            }
        }
    }

    public boolean addBooking() {
        return bookingDAO.create(this);
    }

    public boolean deleteBooking() {
        return bookingDAO.delete(getId());
    }

    public static Booking getBookingById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");

        Booking booking = bookingDAO.getById(id);
        if (booking != null) {
            booking.chargerRelations();
        }
        return booking;
    }

    public static List<Booking> getAllBookings() {
        List<Booking> bookings = bookingDAO.getAll();
        for (Booking booking : bookings) {
            booking.chargerRelations();
        }
        return bookings;
    }

    public static List<Booking> getBookingsByLessonId(int lessonId) {
        List<Booking> bookings = bookingDAO.getBookingsByLessonId(lessonId);
        for (Booking booking : bookings) {
            booking.chargerRelations();
        }
        return bookings;
    }

    public double getTotalPrice() {
        double lessonPrice = this.getLesson().getLessonPrice(this.getLesson().getLessonType());

        if (this.isHasInsurance()) lessonPrice += 20;
        if (this.isHasDiscount()) lessonPrice *= 0.85;

        return lessonPrice;
    }

    public boolean isEligibleForLesson() {
        int studentAge = this.student.getAge();
        Accreditation lessonAccreditation = this.lesson.getLessonType().getAccreditation();
        int accreditationId = lessonAccreditation.getId();

        if (studentAge >= 4 && studentAge <= 12) {
            if (accreditationId != 1 && accreditationId != 3) return false;
        } else {
            if (accreditationId == 1 || accreditationId == 3) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Booking : id=" + id + ", hasInsurance=" + hasInsurance + ", hasDiscount=" + hasDiscount;
    }
}
