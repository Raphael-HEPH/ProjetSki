package be.couder.ecoleski.DAO_Folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Instructor;
import be.couder.ecoledeski.Lesson;
import be.couder.ecoledeski.LessonType;

public class LessonDAO {

    private Connection connection;

    public LessonDAO() {
        this.connection = ConnectToDB.getInstance(); 
    }

    public boolean create(Lesson lesson) {
        String sql = "INSERT INTO lesson (ID, MINSTUDENT, MAXSTUDENT, LESSONTYPEID, LESSONDATE, INSTRUCTORID, "
                   + "STARTTIME, DURATION, ISPRIVATE) "
                   + "VALUES (lesson_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lesson.getMinStudent());
            stmt.setInt(2, lesson.getMaxStudent());
            stmt.setInt(3, lesson.getLessonType().getId());
            stmt.setDate(4, java.sql.Date.valueOf(lesson.getDate()));
            stmt.setInt(5, lesson.getInstructor().getId());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(lesson.getStartHour()));
            stmt.setInt(7, lesson.getMinutes());
            stmt.setInt(8, lesson.isPrivate() ? 1 : 0);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Lesson getById(int id) {
        String sql = "SELECT * FROM lesson WHERE ID = ?";
        Lesson lesson = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int lessonTypeId = rs.getInt("LESSONTYPEID");
                int instructorId = rs.getInt("INSTRUCTORID");

                LessonType lessonType = LessonType.getLessonTypeById(
                    lessonTypeId);

                Instructor instructor = new InstructorDAO().getById(instructorId);

                java.sql.Date date = rs.getDate("LESSONDATE");
                java.time.LocalDate lessonDate = (date != null) ? date.toLocalDate() : null;

                java.sql.Timestamp ts = rs.getTimestamp("STARTTIME");
                java.time.LocalDateTime startTime = (ts != null) ? ts.toLocalDateTime() : null;

                lesson = new Lesson(
                    rs.getInt("ID"),
                    rs.getInt("MINSTUDENT"),
                    rs.getInt("MAXSTUDENT"),
                    lessonDate,
                    startTime,
                    rs.getInt("DURATION"),
                    rs.getInt("ISPRIVATE") == 1,
                    lessonType,
                    instructor
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public List<Lesson> getAll() {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM lesson";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int lessonTypeId = rs.getInt("LESSONTYPEID");
                int instructorId = rs.getInt("INSTRUCTORID");

                LessonType lessonType = LessonType.getLessonTypeById(
                    lessonTypeId
                );

                Instructor instructor = new InstructorDAO().getById(instructorId);

                java.sql.Date date = rs.getDate("LESSONDATE");
                java.time.LocalDate lessonDate = (date != null) ? date.toLocalDate() : null;

                java.sql.Timestamp ts = rs.getTimestamp("STARTTIME");
                java.time.LocalDateTime startTime = (ts != null) ? ts.toLocalDateTime() : null;

                Lesson lesson = new Lesson(
                    rs.getInt("ID"),
                    rs.getInt("MINSTUDENT"),
                    rs.getInt("MAXSTUDENT"),
                    lessonDate,
                    startTime,
                    rs.getInt("DURATION"),
                    rs.getInt("ISPRIVATE") == 1,
                    lessonType,
                    instructor
                );

                lessons.add(lesson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessons;
    }


    public boolean delete(int id) {
        String sql = "DELETE FROM lesson WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Lesson> getLessonsByInstructorId(int instructorId) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM lesson WHERE INSTRUCTORID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int lessonTypeId = rs.getInt("LESSONTYPEID");

                LessonType lessonType = new LessonType();
                lessonType.setId(lessonTypeId);

                Instructor instructor = new Instructor();
                instructor.setId(instructorId);

                java.sql.Timestamp ts = rs.getTimestamp("STARTTIME");
                java.time.LocalDateTime startTime = (ts != null) ? ts.toLocalDateTime() : null;

                java.sql.Date date = rs.getDate("LESSONDATE");
                java.time.LocalDate lessonDate = (date != null) ? date.toLocalDate() : null;

                Lesson lesson = new Lesson(
                    rs.getInt("ID"),
                    rs.getInt("MINSTUDENT"),
                    rs.getInt("MAXSTUDENT"),
                    lessonDate,
                    startTime,
                    rs.getInt("DURATION"),
                    rs.getInt("ISPRIVATE") == 1,
                    lessonType,
                    instructor
                );

                lessons.add(lesson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessons;
    }

    public List<Lesson> getLessonsByLessonTypeId(int lessonTypeId) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM lesson WHERE LESSONTYPEID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lessonTypeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int instructorId = rs.getInt("INSTRUCTORID");

                LessonType lessonType = new LessonType();
                lessonType.setId(lessonTypeId);

                Instructor instructor = new Instructor();
                instructor.setId(instructorId);

                Lesson lesson = new Lesson(
                    rs.getInt("ID"),
                    rs.getInt("MINSTUDENT"),
                    rs.getInt("MAXSTUDENT"),
                    rs.getDate("LESSONDATE").toLocalDate(),
                    rs.getTimestamp("STARTTIME").toLocalDateTime(),
                    rs.getInt("DURATION"),
                    rs.getInt("ISPRIVATE") == 1,
                    lessonType,
                    instructor
                );

                lessons.add(lesson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessons;
    }

}
