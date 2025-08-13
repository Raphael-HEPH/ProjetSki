package be.couder.ecoledeski;

import java.time.LocalDate;

public class Period {

	private int id;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isVacation;
	
	public Period(int id, LocalDate startDate, LocalDate endDate, boolean isVacation) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isVacation = isVacation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		 if (startDate == null) {
		        throw new IllegalArgumentException("La startDate ne peut pas etre null.");
		    }
		    if (this.endDate != null && startDate.isAfter(this.endDate)) {
		        throw new IllegalArgumentException("startDate ne peut pas etre apres endDate.");
		    }
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		 if (endDate == null) {
		        throw new IllegalArgumentException("endDate ne peut pas etre null.");
		    }
		    if (this.startDate != null && endDate.isBefore(this.startDate)) {
		        throw new IllegalArgumentException("endDate ne peut pas etre avant startDate.");
		    }
		this.endDate = endDate;
	}

	public boolean isVacation() {
		return isVacation;
	}

	public void setVacation(boolean isVacation) {
		this.isVacation = isVacation;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Period period = (Period) o;
	    return id == period.id;
	}

	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}

	
	@Override
	public String toString() {
	    return "Period : id : " + id + " startDate : " + startDate + " - endDate : " + endDate ;            
	}
}	
