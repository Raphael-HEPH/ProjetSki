package be.couder.ecoleski.DAO_Folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Student;

public class StudentDAO {

	private Connection connection;
	
	public StudentDAO() {
		this.connection = ConnectToDB.getInstance(); 
	}
	
	 public boolean create(Student student) {
	        String query = "INSERT INTO Student (ID, FIRSTNAME, LASTNAME, AGE, EMAIL, PHONE, ADDRESS) VALUES (seq_student_id.NEXTVAL, ?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, student.getFirstName());
	            statement.setString(2, student.getLastName());
	            statement.setInt(3, student.getAge());
	            statement.setString(4, student.getEmail());
	            statement.setString(5, student.getPhone());
	            statement.setString(6, student.getAddress());
	            
				int rowsAffected = statement.executeUpdate();

				if(rowsAffected > 0) {
					System.out.println("Student crée avec succès.");
					return true;
				}

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	 
	 public boolean delete(String firstName, String lastName) {
	        String checkBookingQuery = "SELECT COUNT(*) FROM BOOKING WHERE STUDENTID = (SELECT id FROM STUDENT WHERE FIRSTNAME = ? AND LASTNAME = ?)";
	        
	        String deleteBookingQuery = "DELETE FROM BOOKING WHERE STUDENTID = (SELECT id FROM STUDENT WHERE FIRSTNAME = ? AND LASTNAME = ?)";
	        String deleteStudentQuery = "DELETE FROM STUDENT WHERE FIRSTNAME = ? AND LASTNAME = ?";
	        
	        try {
	            try (PreparedStatement checkBookingStatement = connection.prepareStatement(checkBookingQuery)) {
	                checkBookingStatement.setString(1, firstName);
	                checkBookingStatement.setString(2, lastName);
	                
	                ResultSet rs = checkBookingStatement.executeQuery();
	                if (rs.next() && rs.getInt(1) > 0) {
	                    try (PreparedStatement deleteBookingStatement = connection.prepareStatement(deleteBookingQuery)) {
	                        deleteBookingStatement.setString(1, firstName);
	                        deleteBookingStatement.setString(2, lastName);
	                        deleteBookingStatement.executeUpdate();
	                    }
	                }
	            }
	            
	            try (PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentQuery)) {
	                deleteStudentStatement.setString(1, firstName);
	                deleteStudentStatement.setString(2, lastName);
	                
	                int rowsAffected = deleteStudentStatement.executeUpdate();
	                return rowsAffected > 0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 
	 public boolean deleteById(int id) {
			String sql = "DELETE FROM student WHERE id = ?";
			
			try(PreparedStatement stmt = connection.prepareStatement(sql)){
				stmt.setInt(1, id);
				int rowsAffected = stmt.executeUpdate();
				
				return rowsAffected > 0;
		
			} catch(SQLException e) {
					e.printStackTrace();
					return false;
			}	
		}
	 
	 public List<Student> GetAll(){
			List<Student> students = new ArrayList<>();
			String sql = "SELECT * FROM student";
			
			try(PreparedStatement stmt = connection.prepareStatement(sql)){
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					Student student = new Student(
							rs.getInt("ID"),
							rs.getString("FIRSTNAME"),
							rs.getString("LASTNAME"),
							rs.getInt("AGE"),
							rs.getString("EMAIL"),
							rs.getString("PHONE"),
							rs.getString("ADDRESS")
							);
					students.add(student);
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return students;
		}
	 
	 public Student GetById(int id) {
			Student student = null;
			String sql = "SELECT * FROM student WHERE id = ?";
			
			try(PreparedStatement stmt = connection.prepareStatement(sql)){
				stmt.setInt(1, id);
				ResultSet rs = stmt.executeQuery();
				
				if(rs.next()) {
					student = new Student(
							rs.getInt("ID"),
							rs.getString("FIRSTNAME"),
							rs.getString("LASTNAME"),
							rs.getInt("AGE"),
							rs.getString("EMAIL"),
							rs.getString("PHONE"),
							rs.getString("ADDRESS")
							);
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return student;
		}
}
