package be.couder.ecoledeski;

public class Booking {

	private int id;
    private boolean hasInsurance;
    private boolean hasDiscount;
    
	public Booking(int id, boolean hasInsurance, boolean hasDiscount) {
		this.id = id;
		this.hasInsurance = hasInsurance;
		this.hasDiscount = hasDiscount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(boolean hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public boolean isHasDiscount() {
		return hasDiscount;
	}

	public void setHasDiscount(boolean hasDiscount) {
		this.hasDiscount = hasDiscount;
	}
    
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Booking booking = (Booking) o;
	    return id == booking.id;
	}

	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}

	@Override
	public String toString() {
	    return "Booking : id=" + id + ", hasInsurance=" + hasInsurance +", hasDiscount=" + hasDiscount ;
	}

    
}
