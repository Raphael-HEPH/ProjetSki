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
    private AccreditationDAO accreditationDAO; 

	public InstructorDAO() {
		this.connection = ConnectToDB.getInstance(); 
        this.accreditationDAO = new AccreditationDAO(); 
	}
	
	
	public boolean create(Instructor instructor) {
     
		String sql = "INSERT INTO instructor (ID, FIRSTNAME, LASTNAME, EMAIL, PHONE, AGE, ADDRESS) " +
                "VALUES (instructor_id_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
		
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        	stmt.setString(1, instructor.getFirstName());
	        stmt.setString(2, instructor.getLastName());
	        stmt.setString(3, instructor.getEmail());
	        stmt.setString(4, instructor.getPhone());
	        stmt.setInt(5, instructor.getAge());
	        stmt.setString(6, instructor.getAddress());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                String selectSql = "SELECT ID FROM instructor WHERE EMAIL = ?";

                try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                    selectStmt.setString(1, instructor.getEmail());

                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            int id = rs.getInt("ID");
                            instructor.setId(id);

                            for (Accreditation accreditation : instructor.getAccreditations()) {
                                addAccreditationToInstructor(id, accreditation.getId());
                            }
System.out.println("Instructeur inséré avec ID: " + id);
                            return true;
                        } else {
                            System.out.println("Échec récupération ID après insertion.");
                        }
                    }
                }
            } else {
                System.out.println("Aucune ligne insérée.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


	public void addAccreditationToInstructor(int instructorId, int accreditationId) {
	    String sql = "INSERT INTO instructor_accreditation (INSTRUCTOR_ID, ACCREDITATION_ID) VALUES(?, ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, instructorId);
	        stmt.setInt(2, accreditationId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public boolean deleteById(int id) {
		String deleteAccSql = "DELETE FROM instructor_accreditation WHERE INSTRUCTOR_ID = ?";
		String deleteInstructorSql = "DELETE FROM instructor WHERE ID = ?";


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
	        String sql = "SELECT * FROM instructor WHERE ID = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                Accreditation acc = new Accreditation(0, null);
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
		            int instructorId = rs.getInt("ID");

		            List<Accreditation> accs = accreditationDAO.getAccreditationsByInstructorId(instructorId);

		            Accreditation firstAcc;
		            if (accs.isEmpty()) {
		                firstAcc = new Accreditation(0, "Non définie");
		            } else {
		                firstAcc = accs.get(0);
		            }

		            Instructor instructor = new Instructor(
		                instructorId,
		                rs.getString("FIRSTNAME"),
		                rs.getString("LASTNAME"),
		                rs.getInt("AGE"),
		                rs.getString("EMAIL"),
		                rs.getString("PHONE"),
		                rs.getString("ADDRESS"),
		                firstAcc
		            );

		            for (int i = 1; i < accs.size(); i++) {
		                instructor.addAccreditation(accs.get(i));
		            }

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
		             "JOIN instructor_accreditation ia ON i.ID = ia.INSTRUCTOR_ID " +
		             "WHERE ia.ACCREDITATION_ID = ?";


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
