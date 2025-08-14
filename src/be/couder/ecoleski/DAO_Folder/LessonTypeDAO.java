package be.couder.ecoleski.DAO_Folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoledeski.Accreditation;
import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.LessonType;

public class LessonTypeDAO {

	private Connection connection;
	
	public LessonTypeDAO() {
		this.connection = ConnectToDB.getInstance(); 
	}
	
	public boolean create(LessonType lessonType) {
        String sql = "INSERT INTO LessonType (NAME, PRICE_PER_WEEK, ACCREDITATION_ID, LESSONLEVEL) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lessonType.getName());
            stmt.setDouble(2, lessonType.getPrice());
            stmt.setInt(3, lessonType.getAccreditation().getId());
            stmt.setInt(4, lessonType.getLessonLevel());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public LessonType getById(int id) {
        String sql = "SELECT * FROM LessonType WHERE ID = ?";
        LessonType lessonType = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int accreditationId = rs.getInt("ACCREDITATION_ID");
                Accreditation accreditation = new Accreditation(accreditationId, null);

                lessonType = new LessonType(
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getDouble("PRICE_PER_WEEK"),
                    rs.getInt("LESSONLEVEL"),
                    accreditation
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessonType;
    }
	
	 public List<LessonType> getAll() {
	        List<LessonType> lessonTypes = new ArrayList<>();
	        String sql = "SELECT * FROM LessonType";

	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                int accreditationId = rs.getInt("ACCREDITATION_ID");
	                Accreditation accreditation = new Accreditation(accreditationId, null);

	                LessonType lessonType = new LessonType(
	                    rs.getInt("ID"),
	                    rs.getString("NAME"),
	                    rs.getDouble("PRICE_PER_WEEK"),
	                    rs.getInt("LESSONLEVEL"),
	                    accreditation
	                );

	                lessonTypes.add(lessonType);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lessonTypes;
	 }
	 
	 public boolean delete(int id) {
	        String sql = "DELETE FROM LessonType WHERE ID = ?";

	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            int rowsAffected = stmt.executeUpdate();

	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	 }
	 
	 public List<LessonType> getLessonTypesByAccreditationId(int accreditationId) {
	        List<LessonType> lessonTypes = new ArrayList<>();
	        String sql = "SELECT * FROM LessonType WHERE ACCREDITATION_ID = ?";

	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setInt(1, accreditationId);
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                Accreditation accreditation = new Accreditation(accreditationId, null);
	                LessonType lessonType = new LessonType(
	                    rs.getInt("ID"),
	                    rs.getString("NAME"),
	                    rs.getDouble("PRICE_PER_WEEK"),
	                    rs.getInt("LESSONLEVEL"),
	                    accreditation
	                );
	                lessonTypes.add(lessonType);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return lessonTypes;
	    }
	 

}
