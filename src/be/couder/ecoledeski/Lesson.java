package be.couder.ecoledeski;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.AccreditationDAO;
import be.couder.ecoleski.DAO_Folder.InstructorDAO;
import be.couder.ecoleski.DAO_Folder.LessonDAO;
import be.couder.ecoleski.DAO_Folder.LessonTypeDAO;

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

    private static final LessonDAO lessonDAO = new LessonDAO();
    private static final InstructorDAO instructorDAO = new InstructorDAO();
    private static final LessonTypeDAO lessonTypeDAO = new LessonTypeDAO();
    private static final AccreditationDAO accreditationDAO = new AccreditationDAO();

    public Lesson() {
        bookings = new ArrayList<>();
    }

    public Lesson(int id, int minStudent, int maxStudent, LocalDate date, LocalDateTime startHour, int minutes,
                  boolean isPrivate, LessonType lessonType, Instructor instructor) {
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
	    if (this.instructor == instructor) {
	        return;
	    }

	    if (this.instructor != null) {
	        Instructor oldInstructor = this.instructor;
	        this.instructor = null; 
	        oldInstructor.getLessons().remove(this);
	    }

	    this.instructor = instructor;

	    if (instructor != null && !instructor.getLessons().contains(this)) {
	        instructor.getLessons().add(this);
	    }
	}



	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public void chargerRelations() {
        if (this.id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");

        if (this.instructor != null) {
            Instructor fullInstructor = instructorDAO.getById(this.instructor.getId());
            this.setInstructor(fullInstructor);
        }

        if (this.lessonType != null) {
            LessonType fullLessonType = lessonTypeDAO.getById(this.lessonType.getId());
            if (fullLessonType.getAccreditation() == null && this.lessonType.getAccreditation() != null) {
                Accreditation fullAccreditation = accreditationDAO.getById(this.lessonType.getAccreditation().getId());
                fullLessonType.setAccreditation(fullAccreditation);
            }
            this.setLessonType(fullLessonType);
        }

        List<Booking> loadedBookings = Booking.getBookingsByLessonId(this.id);
        this.bookings.clear();
        for (Booking booking : loadedBookings) {
            booking.setLesson(this);
            this.bookings.add(booking);
        }
    }

    public boolean addLesson() { return lessonDAO.create(this); }

    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            if (!this.canAddCollectiveBooking())
                throw new IllegalStateException("Le nombre maximum d'élèves pour ce cours est atteint.");
            bookings.add(booking);
            booking.setLesson(this);
        }
    }

    public static Lesson getLessonById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit etre plus grand que 0.");
        Lesson lesson = lessonDAO.getById(id);
        if (lesson != null && lesson.getInstructor() != null) {
            Instructor instructor = instructorDAO.getById(lesson.getInstructor().getId());
            lesson.setInstructor(instructor);
        }
        return lesson;
    }

    public static List<Lesson> getAllLessons() {
        List<Lesson> lessons = lessonDAO.getAll();
        for (Lesson lesson : lessons) {
            if (lesson.getInstructor() != null) {
                Instructor instructor = instructorDAO.getById(lesson.getInstructor().getId());
                lesson.setInstructor(instructor);
            }
        }
        return lessons;
    }

    public static boolean deleteLessonById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");
        return lessonDAO.delete(id);
    }

    public static List<Lesson> getLessonsByInstructorId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");
        return lessonDAO.getLessonsByInstructorId(id);
    }

    public boolean isInstructorAccredited() {
        if (instructor == null || lessonType == null) return false;
        return instructor.hasAccreditation(lessonType);
    }

    public double getLessonPrice(LessonType lessonType) { return lessonType.getPrice(); }

    public boolean canAddCollectiveBooking() {
        if (this.isPrivate) return true;

        int currentStudents = this.bookings.size();
        int min, max;

        if (this.lessonType == null || this.lessonType.getAccreditation() == null) {
            System.err.println("Warning: LessonType ou Accreditation non défini pour la leçon id=" + this.getId());
            min = 1;
            max = 10;
        } else {
            String accreditationName = this.lessonType.getAccreditation().getName();
            switch (accreditationName) {
                case "Enfant", "Ski Enfant", "Compétition", "Hors-piste" -> { min = 5; max = 8; }
                case "Adulte", "Ski Adulte" -> { min = 6; max = 10; }
                case "Télémark", "Ski de Fond" -> { min = 1; max = 4; }
                default -> throw new IllegalStateException("Accréditation inconnue : " + accreditationName);
            }
        }

        return currentStudents < max;
    }

    @Override
    public String toString() {
        return "Leçon : id=" + id + ", minStudent=" + minStudent + ", maxStudent=" + maxStudent +
               ", date=" + date + ", startHour=" + startHour + ", private=" + isPrivate;
    }
}
