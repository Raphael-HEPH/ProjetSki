package be.couder.ecoledeski;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.BookingDAO;
import be.couder.ecoleski.DAO_Folder.PeriodDAO;

public class Period {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isVacation;
    private List<Booking> bookings;

   
    private static final PeriodDAO periodDAO = new PeriodDAO();
    private static final BookingDAO bookingDAO = new BookingDAO();

    public Period() {
        bookings = new ArrayList<>();
    }

    public Period(int id, LocalDate startDate, LocalDate endDate, boolean isVacation) {
        this.id = id;
        setStartDate(startDate);
        setEndDate(endDate);
        this.isVacation = isVacation;
        bookings = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) {
        if (startDate == null) throw new IllegalArgumentException("La startDate ne peut pas être null.");
        if (this.endDate != null && startDate.isAfter(this.endDate))
            throw new IllegalArgumentException("startDate ne peut pas être après endDate.");
        this.startDate = startDate;
    }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) {
        if (endDate == null) throw new IllegalArgumentException("endDate ne peut pas être null.");
        if (this.startDate != null && endDate.isBefore(this.startDate))
            throw new IllegalArgumentException("endDate ne peut pas être avant startDate.");
        this.endDate = endDate;
    }

    public boolean isVacation() { return isVacation; }
    public void setVacation(boolean isVacation) { this.isVacation = isVacation; }

    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
            if (booking.getPeriod() != this) booking.setPeriod(this);
        }
    }

    public void chargerRelation() {
        if (this.id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");

        List<Booking> loadedBookings = bookingDAO.getBookingsByPeriodId(this.id);
        this.bookings.clear();
        for (Booking booking : loadedBookings) {
            booking.setPeriod(this);
            this.bookings.add(booking);
        }
    }

    public boolean addPeriod() {
        return periodDAO.create(this);
    }

    public boolean deletePeriod() {
        if (id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");
        return periodDAO.delete(this.id);
    }

    public static Period getPeriodById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");
        Period period = periodDAO.getById(id);
        if (period != null) period.chargerRelation();
        return period;
    }

    public static List<Period> getAllPeriods() {
        List<Period> periods = periodDAO.getAll();
        for (Period period : periods) period.chargerRelation();
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return id == period.id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public String toString() {
        return "startDate : " + startDate + " - endDate : " + endDate;
    }
}
