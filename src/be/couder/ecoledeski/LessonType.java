package be.couder.ecoledeski;

public class LessonType {

	private int id;
	private double price;
	private String name;
	
	public LessonType(int id, double price, String name) {
		this.id = id;
		this.price = price;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price < 0) {
	        throw new IllegalArgumentException("Le prix doit etre positif.");
	    }
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom ne peut pas etre null ou vide.");
	    }
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    LessonType that = (LessonType) o;
	    return id == that.id;
	}

	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}
	
	@Override
	public String toString() {
	    return "LessonType : id :" + id + " nom : " + name + " prix : " + price;
	}

}
