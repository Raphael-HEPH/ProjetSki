package be.couder.ecoledeski;

public class Instructor extends Person{

	 public Instructor(int id, String firstName, String lastName, int age, String email, String phone, String address) {
		 super(id, firstName, lastName, age, email, phone, address);
	}
	 
	 @Override
		public String toString() {
			return "Instructor : " + super.toString();
		}
}
