package be.couder.ecoledeski;

import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.BookingDAO;
import be.couder.ecoleski.DAO_Folder.StudentDAO;

public class Student extends Person {

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final BookingDAO bookingDAO = new BookingDAO();

    private List<Booking> bookings;

    public Student() {
        super();
        this.bookings = new ArrayList<>();
    }

    public Student(int id, String firstName, String lastName, int age, String email, String phone, String address) {
        super(id, firstName, lastName, age, email, phone, address);
        this.bookings = new ArrayList<>();
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
            if (booking.getStudent() != this) {
                booking.setStudent(this);
            }
        }
    }

    public void chargerRelation() {
        this.bookings = bookingDAO.getBookingsByStudentId(this.getId());
        for (Booking booking : bookings) {
            if (booking.getStudent() != this) {
                booking.setStudent(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Student : " + super.toString();
    }

    public boolean addStudent() {
        return studentDAO.create(this);
    }

    public boolean deleteStudent() {
        return studentDAO.delete(getFirstName(), getLastName());
    }

    public boolean deleteStudentById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id doit être plus grand que 0.");
        }
        return studentDAO.deleteById(id);
    }

    public static Student getStudentById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id doit être plus grand que 0.");
        }
        return studentDAO.GetById(id);
    }

    public static List<Student> getAllStudent() {
        return studentDAO.GetAll();
    }

    public boolean deleteStudentByName(String name, String firstName) {
        return studentDAO.delete(name, firstName);
    }
}
