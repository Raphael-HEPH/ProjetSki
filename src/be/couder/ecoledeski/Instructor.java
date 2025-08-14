package be.couder.ecoledeski;

import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.AccreditationDAO;
import be.couder.ecoleski.DAO_Folder.InstructorDAO;
import be.couder.ecoleski.DAO_Folder.LessonDAO;

public class Instructor extends Person{
	
	private List<Accreditation> accreditations;
	private List<Lesson> lessons;
	private List<Booking> bookings;
	
	public Instructor() {
		super();
		accreditations = new ArrayList<>();
        lessons = new ArrayList<>();
	    bookings = new ArrayList<>();
	}

	 public Instructor(int id, String firstName, String lastName, int age, String email, String phone, String address, Accreditation accreditation) {
		 super(id, firstName, lastName, age, email, phone, address);
		 accreditations = new ArrayList<>();
	     lessons = new ArrayList<>();
	     bookings = new ArrayList<>();
	     
	     if(accreditation == null)
	    	 throw new IllegalArgumentException("accreditation ne peut pas etre null.");
	     
	     addAccreditation(accreditation);
	}
	 
	 
	 public List<Accreditation> getAccreditations() {
		return accreditations;
	}

	public void setAccreditations(List<Accreditation> accreditations) {
		this.accreditations = accreditations;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}
	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public void addAccreditation(Accreditation accreditation) {
	    if (accreditation != null && !accreditations.contains(accreditation)) {
	        accreditations.add(accreditation);
	        if (!accreditation.getInstructors().contains(this)) {
	            accreditation.addInstructor(this);
	        }
	    }
	}
	
	public void removeAccreditation(Accreditation accreditation) {
	    if (accreditation != null && accreditations.contains(accreditation)) {
	        accreditations.remove(accreditation);
	        accreditation.getInstructors().remove(this);
	    }
	}

	
	public void addBooking(Booking booking) {
	    if (booking == null) {
	        throw new IllegalArgumentException("Le booking ne peut pas être null.");
	    }
	    if (!bookings.contains(booking)) {
	        bookings.add(booking);
	        booking.setInstructor(this); 
	    }
	}

	
	public void addLesson(Lesson lesson) {
		if(!lessons.contains(lesson)) {
			lessons.add(lesson);
			lesson.setInstructor(this);
		}
	}

	public boolean addInstructor(InstructorDAO instructorDAO) {
		return instructorDAO.create(this);
	}
	
	public boolean deleteInstructorById(int id, InstructorDAO instructorDAO) {
		if (id <= 0) {
            throw new IllegalArgumentException("Id doit etre plus grand que 0.");
        }
		return instructorDAO.deleteById(id);
	}
	
	public void loadRelations(AccreditationDAO accreditationDAO, LessonDAO lessonDAO) {
	    if (this.getId() <= 0) {
	        throw new IllegalArgumentException("Id plus petit ou égal à 0");
	    }

	    List<Accreditation> loadedAccreditations = accreditationDAO.getAccreditationsByInstructorId(this.getId());
	    this.accreditations.clear(); 
	    this.accreditations.addAll(loadedAccreditations);

	    List<Lesson> loadedLessons = lessonDAO.getLessonsByInstructorId(this.getId());
	    this.lessons.clear();
	    for (Lesson lesson : loadedLessons) {
	        lesson.setInstructor(this); 
	        this.lessons.add(lesson);
	    }
	}

	public static Instructor getInstructorById(int id, InstructorDAO instructorDAO, AccreditationDAO accreditationDAO, LessonDAO lessonDAO) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id plus petit ou égal à 0");
        }
	    Instructor instructor = instructorDAO.getById(id);
	    if (instructor != null) {
	        instructor.loadRelations(accreditationDAO, lessonDAO);
	    }
	    return instructor;
	}
	
	public static List<Instructor> getAllInstructors(InstructorDAO instructorDAO, AccreditationDAO accreditationDAO, LessonDAO lessonDAO) {
	    List<Instructor> instructors = instructorDAO.getAll();
	    for (Instructor instructor : instructors) {
	        instructor.loadRelations(accreditationDAO, lessonDAO);
	    }
	    return instructors;
	}
	
	public static List<Instructor> getInstructorsByAccreditationId(int accreditationId, InstructorDAO instructorDAO) {
	    if (accreditationId <= 0) {
	        throw new IllegalArgumentException("AccreditationId doit être plus grand que 0.");
	    }
	    return instructorDAO.getInstructorsByAccreditationId(accreditationId);
	}

	
	@Override
		public String toString() {
			return "Instructor : " + super.toString();
		}
}
