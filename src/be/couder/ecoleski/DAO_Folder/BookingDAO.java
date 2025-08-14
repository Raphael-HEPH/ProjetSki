package be.couder.ecoleski.DAO_Folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.couder.ecoledeski.Booking;
import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Lesson;
import be.couder.ecoledeski.Instructor;
import be.couder.ecoledeski.Student;
import be.couder.ecoledeski.Period;

public class BookingDAO {

    private Connection connection;

    public BookingDAO() {
        this.connection = ConnectToDB.getInstance();
    }

    public boolean create(Booking booking) {
        String query = "INSERT INTO Booking (ID, HASINSURANCE, HASDISCOUNT, LESSONID, INSTRUCTORID, STUDENTID, PERIODID) "
                     + "VALUES (BOOKING_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, new String[] {"ID"})) {
            stmt.setInt(1, booking.isHasInsurance() ? 1 : 0);
            stmt.setInt(2, booking.isHasDiscount() ? 1 : 0);

            stmt.setObject(3, booking.getLesson() != null ? booking.getLesson().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(4, booking.getInstructor() != null ? booking.getInstructor().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(5, booking.getStudent() != null ? booking.getStudent().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(6, booking.getPeriod() != null ? booking.getPeriod().getId() : null, java.sql.Types.INTEGER);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    booking.setId(rs.getInt(1));
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");
        String sql = "DELETE FROM Booking WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Booking getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");

        Booking booking = null;
        String sql = "SELECT * FROM Booking WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setId(rs.getInt("LESSONID"));

                Instructor instructor = new Instructor();
                instructor.setId(rs.getInt("INSTRUCTORID"));

                Student student = new Student();
                student.setId(rs.getInt("STUDENTID"));

                Period period = new Period();
                period.setId(rs.getInt("PERIODID"));

                booking = new Booking(
                    rs.getInt("ID"),
                    rs.getInt("HASINSURANCE") == 1,
                    rs.getInt("HASDISCOUNT") == 1,
                    lesson,
                    instructor,
                    student,
                    period
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }

    public List<Booking> getAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setId(rs.getInt("LESSONID"));

                Instructor instructor = new Instructor();
                instructor.setId(rs.getInt("INSTRUCTORID"));

                Student student = new Student();
                student.setId(rs.getInt("STUDENTID"));

                Period period = new Period();
                period.setId(rs.getInt("PERIODID"));

                Booking booking = new Booking(
                    rs.getInt("ID"),
                    rs.getInt("HASINSURANCE") == 1,
                    rs.getInt("HASDISCOUNT") == 1,
                    lesson,
                    instructor,
                    student,
                    period
                );

                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public List<Booking> getBookingsByLessonId(int lessonId) {
	    if (lessonId <= 0) throw new IllegalArgumentException("Id de leçon doit être plus grand que 0.");

	    List<Booking> bookings = new ArrayList<>();
	    String sql = "SELECT * FROM Booking WHERE LESSONID = ?";

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, lessonId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Lesson lesson = new Lesson();
	            lesson.setId(rs.getInt("LESSONID"));

	            Instructor instructor = new Instructor();
	            instructor.setId(rs.getInt("INSTRUCTORID"));

	            Student student = new Student();
	            student.setId(rs.getInt("STUDENTID"));

	            Period period = new Period();
	            period.setId(rs.getInt("PERIODID"));

	            Booking booking = new Booking(
	                rs.getInt("ID"),
	                rs.getInt("HASINSURANCE") == 1,
	                rs.getInt("HASDISCOUNT") == 1,
	                lesson,
	                instructor,
	                student,
	                period
	            );

	            bookings.add(booking);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return bookings;
	}
    
}
