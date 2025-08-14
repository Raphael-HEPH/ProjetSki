package be.couder.ecoledeski;

import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.StudentDAO;

public class Student extends Person {
	
	public Student() {
		super();
	}
	
	private List<Booking> bookings;
	
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


	@Override
	public String toString() {
		return "Student : " + super.toString();
	}
	
	public boolean addStudent(StudentDAO studentDAO) {
		return studentDAO.create(this);
	}
	
	public boolean deleteStudent(StudentDAO studentDAO) {
		return studentDAO.delete(getFirstName(), getLastName());
	}
	
	public boolean deleteStudentById(int id, StudentDAO studentDAO) {
		if (id <= 0) {
            throw new IllegalArgumentException("Id doit etre plus grand que 0.");
        }
		return studentDAO.deleteById(id);
	}
	
	public static Student getStudentById(int id, StudentDAO studentDAO) {
		if (id <= 0) {
            throw new IllegalArgumentException("Id doit etre plus grand que 0.");
        }
		return studentDAO.GetById(id);
	}
	
	public static List<Student> getAllStudent(StudentDAO studentDAO){
		return studentDAO.GetAll();
	}
}
