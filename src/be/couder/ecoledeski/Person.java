package be.couder.ecoledeski;

public abstract class Person {

	private int id;
	private String firstName;
	private String lastName;
	private int age;
	private String email;
	private String phone;
	private String address;
	
	public Person(int id, String firstName, String lastName, int age, String email, String phone, String address) {
		this.id = id;
		this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.address = address;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		if(firstName == null || firstName.trim().isEmpty()) {
			throw new IllegalArgumentException("Le firstName ne peut pas etre null ou vide.");
		}
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		if(lastName == null || lastName.trim().isEmpty()) {
			throw new IllegalArgumentException("Le lastName ne peut pas etre null ou vide.");
		}
		this.lastName = lastName;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		 if (age < 0) 
		     throw new IllegalArgumentException("L'age doit etre positif.");
		 
		this.age = age;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if (email == null || !email.contains("@")) 
	        throw new IllegalArgumentException("Format d'email invalid.");
	    
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		if (phone == null || phone.trim().isEmpty())
		    throw new IllegalArgumentException("Le phone ne peut pas être vide.");
		
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		if (phone == null || phone.trim().isEmpty())
		    throw new IllegalArgumentException("L'adresse ne peut pas être vide.");

		this.address = address;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Person person = (Person) o;
	    return id == person.id;
	}

	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}

	
	 @Override
	    public String toString() {
	        return " Nom :" + lastName + " Prenom : "+ firstName 
	        		+"\n Age : "+ age + "ans" + " email : " + email;  
	    }
}
