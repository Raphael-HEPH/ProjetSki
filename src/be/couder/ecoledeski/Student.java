package be.couder.ecoledeski;

public class Student extends Person {

	public Student(int id, String firstName, String lastName, int age, String email, String phone, String address) {
        super(id, firstName, lastName, age, email, phone, address);
    }

	@Override
	public String toString() {
		return "Student : " + super.toString();
	}
	
	
}
