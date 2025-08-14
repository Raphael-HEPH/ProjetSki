package be.couder.ecoledeski;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.BookingDAO;
import be.couder.ecoleski.DAO_Folder.InstructorDAO;
import be.couder.ecoleski.DAO_Folder.LessonDAO;
import be.couder.ecoleski.DAO_Folder.LessonTypeDAO;
import be.couder.ecoleski.DAO_Folder.PeriodDAO;
import be.couder.ecoleski.DAO_Folder.StudentDAO;

public class Lesson {

	private int id;
	private int minStudent;
	private int maxStudent;
	
	private LocalDate date;
	private LocalDateTime startHour;
	private int minutes;
	private boolean isPrivate;
	
	private LessonType lessonType;
	private Instructor instructor;
	private List<Booking> bookings;
	
	public Lesson() {
        bookings = new ArrayList<>();
	}
	public Lesson(int id, int minStudent, int maxStudent, LocalDate date, LocalDateTime startHour, int minutes
			, boolean isPrivate, LessonType lessonType, Instructor instructor) 
	{
		this.id = id;
		this.minStudent = minStudent;
		this.maxStudent = maxStudent;
		this.date = date;
		this.startHour = startHour;
        this.minutes = minutes;
        this.isPrivate = isPrivate;
        this.lessonType = lessonType;
        this.instructor = instructor;
        bookings = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinStudent() {
		return minStudent;
	}

	public void setMinStudent(int minStudent) {
		if (maxStudent < 1) {
	        throw new IllegalArgumentException("Il doit y avoir au moins 1 student.");
	    }
		if (this.maxStudent > 0 && minStudent > this.maxStudent) {
	        throw new IllegalArgumentException("Le minimum de student ne peut pas depasser le maximum.");
	    }
		this.minStudent = minStudent;
	}

	public int getMaxStudent() {
		return maxStudent;
	}

	public void setMaxStudent(int maxStudent) {
		if (maxStudent < 1) {
	        throw new IllegalArgumentException("Le maximum de student doit être au moins 1.");
	    }
		if (maxStudent < this.minStudent) {
	        throw new IllegalArgumentException("Le max de student ne peut pas etre plus petit que le minimum.");
	    }
		this.maxStudent = maxStudent;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		 if (date == null) {
		        throw new IllegalArgumentException("La date ne peut pas etre null.");
		    }
		this.date = date;
	}

	public LocalDateTime getStartHour() {
		return startHour;
	}

	public void setStartHour(LocalDateTime startHour) {
		this.startHour = startHour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
	    if (this.instructor != null && this.instructor.getLessons().contains(this)) {
	        this.instructor.getLessons().remove(this); 
	    }
	    this.instructor = instructor;
	    if (instructor != null && !instructor.getLessons().contains(this)) {
	        instructor.addLesson(this);
	    }
	}
	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public void ChargerRelation(InstructorDAO instructorDAO, BookingDAO bookingDAO, LessonTypeDAO lessonTypeDAO,PeriodDAO periodDAO,
			LessonDAO lessonDAO, StudentDAO studentDAO) {
	    if (this.id <= 0) {
	        throw new IllegalArgumentException("Id plus petit ou égal à 0");
	    }

	    if (this.instructor != null) {
	        Instructor fullInstructor = instructorDAO.getById(this.instructor.getId());
	        this.setInstructor(fullInstructor);
	    }
	    if (this.lessonType != null) {
	        LessonType fullLessonType = lessonTypeDAO.getById(this.lessonType.getId());
	        this.setLessonType(fullLessonType);
	    }

	    List<Booking> loadedBookings = Booking.getBookingsByLessonId(this.id, bookingDAO, periodDAO, lessonDAO, studentDAO, instructorDAO);
	    this.bookings.clear();
	    for (Booking booking : loadedBookings) {
	        booking.setLesson(this); 
	        this.bookings.add(booking);
	    }
	}



	public boolean addLesson(LessonDAO lessonDAO) {
        return lessonDAO.create(this);
    }
	
	public void addBooking(Booking booking) {
	    if (booking != null && !bookings.contains(booking)) {
	        bookings.add(booking);
	        booking.setLesson(this); 
	    }
	}
    
    public static Lesson getLessonById(int id, LessonDAO lessonDAO, InstructorDAO instructorDAO) {
		if(id <= 0) {
			throw new IllegalArgumentException("Id doit etre plus grand que 0.");
		}
        Lesson lesson = lessonDAO.getById(id);
        if (lesson != null && lesson.getInstructor() != null) {
            Instructor instructor = instructorDAO.getById(lesson.getInstructor().getId());
            lesson.setInstructor(instructor);
        }
        return lesson;
    }
    
    public static List<Lesson> getAllLessons(LessonDAO lessonDAO, InstructorDAO instructorDAO) {
        List<Lesson> lessons = lessonDAO.getAll();
        for (Lesson lesson : lessons) {
            if (lesson.getInstructor() != null) {
                Instructor instructor = instructorDAO.getById(lesson.getInstructor().getId());
                lesson.setInstructor(instructor);
            }
        }
        return lessons;
    }
    
    public static boolean deleteLessonById(int id, LessonDAO lessonDAO) {
		if(id <= 0) {
			throw new IllegalArgumentException("Id plus petit ou égal à 0");
		}
        return lessonDAO.delete(id);
    }
    
    public static List<Lesson> getLessonsByInstructorId(int id, LessonDAO lessonDAO){
		if(id <= 0) {
			throw new IllegalArgumentException("Id plus petit ou égal à 0");
		}
    	return lessonDAO.getLessonsByInstructorId(id);
    }
}
