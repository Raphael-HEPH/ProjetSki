package be.couder.ecoledeski;

import java.time.LocalDateTime;

public class Lesson {

	private int id;
	private int minStudent;
	private int maxStudent;
	private LocalDateTime date;
	
	public Lesson(int id, int minStudent, int maxStudent, LocalDateTime date) {
		this.id = id;
		this.minStudent = minStudent;
		this.maxStudent = maxStudent;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinStudent() {
		return minStudent;
	}

	public void setMinStudent(int minStudent) {
		if (maxStudent < 1) {
	        throw new IllegalArgumentException("Il doit y avoir au moins 1 student.");
	    }
		if (this.maxStudent > 0 && minStudent > this.maxStudent) {
	        throw new IllegalArgumentException("Le minimum de student ne peut pas depasser le maximum.");
	    }
		this.minStudent = minStudent;
	}

	public int getMaxStudent() {
		return maxStudent;
	}

	public void setMaxStudent(int maxStudent) {
		if (maxStudent < this.minStudent) {
	        throw new IllegalArgumentException("Le max de student ne peut pas etre plus petit que le minimum.");
	    }
		this.maxStudent = maxStudent;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		 if (date == null) {
		        throw new IllegalArgumentException("La date ne peut pas etre null.");
		    }
		this.date = date;
	}
	
	
	
}
