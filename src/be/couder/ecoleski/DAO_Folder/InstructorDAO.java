package be.couder.ecoleski.DAO_Folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoledeski.Accreditation;
import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Instructor;

public class InstructorDAO {
	
	private Connection connection;
	
	public InstructorDAO() {
		this.connection = ConnectToDB.getInstance(); 
	}
	
	public boolean create(Instructor instructor) {

	        String sql = "INSERT INTO instructor (FIRSTNAME, LASTNAME, EMAIL, PHONE, AGE, ADDRESS) " +
	                     "VALUES (?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setString(1, instructor.getFirstName());
	            stmt.setString(2, instructor.getLastName());
	            stmt.setString(3, instructor.getEmail());
	            stmt.setString(4, instructor.getPhone());
	            stmt.setInt(5, instructor.getAge());
	            stmt.setString(6, instructor.getAddress());

	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	                // récupérer l'ID généré
	                String selectSql = "SELECT ID FROM instructor WHERE EMAIL = ?";
	                try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
	                    selectStmt.setString(1, instructor.getEmail());
	                    ResultSet rs = selectStmt.executeQuery();
	                    if (rs.next()) {
	                        int id = rs.getInt("ID");
	                        instructor.setId(id);

	                        // ajouter les accréditations
	                        for (Accreditation acc : instructor.getAccreditations()) {
	                            addAccreditationToInstructor(id, acc.getId());
	                        }
	                        System.out.println("Instructor créé avec ID: " + id);
	                        return true;
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return false;
	    }

	    private void addAccreditationToInstructor(int instructorId, int accreditationId) {
	        String sql = "INSERT INTO instructor_accreditation (instructorId, accreditationId) VALUES(?, ?)";
	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setInt(1, instructorId);
	            stmt.setInt(2, accreditationId);
	            stmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	public boolean deleteById(int id) {
	    String deleteAccSql = "DELETE FROM instructor_accreditation WHERE instructorId = ?";
	    String deleteInstructorSql = "DELETE FROM instructor WHERE id = ?";

	    try (
	        PreparedStatement deleteAccStmt = connection.prepareStatement(deleteAccSql);
	        PreparedStatement deleteInstructorStmt = connection.prepareStatement(deleteInstructorSql)
	    ) {
	        deleteAccStmt.setInt(1, id);
	        deleteAccStmt.executeUpdate();

	        deleteInstructorStmt.setInt(1, id);
	        int rowsAffected = deleteInstructorStmt.executeUpdate();
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	 public Instructor getById(int id) {
	        Instructor instructor = null;
	        String sql = "SELECT * FROM instructor WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                Accreditation acc = new Accreditation(0, null); // placeholder
	                instructor = new Instructor(
	                    rs.getInt("ID"),
	                    rs.getString("FIRSTNAME"),
	                    rs.getString("LASTNAME"),
	                    rs.getInt("AGE"),
	                    rs.getString("EMAIL"),
	                    rs.getString("PHONE"),
	                    rs.getString("ADDRESS"),
	                    acc
	                );
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return instructor;
	    }
	 
	 public List<Instructor> getAll() {
	        List<Instructor> instructors = new ArrayList<>();
	        String sql = "SELECT * FROM instructor";
	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                Accreditation acc = new Accreditation(0, null); // placeholder
	                Instructor instructor = new Instructor(
	                    rs.getInt("ID"),
	                    rs.getString("FIRSTNAME"),
	                    rs.getString("LASTNAME"),
	                    rs.getInt("AGE"),
	                    rs.getString("EMAIL"),
	                    rs.getString("PHONE"),
	                    rs.getString("ADDRESS"),
	                    acc
	                );
	                instructors.add(instructor);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return instructors;
	    }
	 
	 public List<Instructor> getInstructorsByAccreditationId(int accreditationId) {
		    List<Instructor> instructors = new ArrayList<>();

		    String sql = "SELECT i.* FROM instructor i " +
		                 "JOIN instructor_accreditation ia ON i.id = ia.instructorId " +
		                 "WHERE ia.accreditationId = ?";

		    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		        stmt.setInt(1, accreditationId);
		        ResultSet rs = stmt.executeQuery();

		        while (rs.next()) {
		            Accreditation acc = new Accreditation(accreditationId, null);

		            Instructor instructor = new Instructor(
		                rs.getInt("ID"),
		                rs.getString("FIRSTNAME"),
		                rs.getString("LASTNAME"),
		                rs.getInt("AGE"),
		                rs.getString("EMAIL"),
		                rs.getString("PHONE"),
		                rs.getString("ADDRESS"),
		                acc
		            );

		            instructors.add(instructor);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return instructors;
		}

}
