package be.couder.ecoleski.DAO_Folder;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Period;

public class PeriodDAO {

    private Connection connection;

    public PeriodDAO() {
        this.connection = ConnectToDB.getInstance();
    }

    public boolean create(Period period) {
        String sql = "INSERT INTO Period (ID, STARTDATE, ENDDATE, ISVACATION, NAME) " +
                     "VALUES (seq_period_id.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(period.getStartDate()));
            stmt.setDate(2, Date.valueOf(period.getEndDate()));
            stmt.setInt(3, period.isVacation() ? 1 : 0);
            stmt.setString(4, null); 

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Period getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");

        Period period = null;
        String sql = "SELECT * FROM Period WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                period = new Period(
                    rs.getInt("ID"),
                    rs.getDate("STARTDATE").toLocalDate(),
                    rs.getDate("ENDDATE").toLocalDate(),
                    rs.getInt("ISVACATION") == 1
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return period;
    }

    public List<Period> getAll() {
        List<Period> periods = new ArrayList<>();
        String sql = "SELECT * FROM Period";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Period period = new Period(
                    rs.getInt("ID"),
                    rs.getDate("STARTDATE").toLocalDate(),
                    rs.getDate("ENDDATE").toLocalDate(),
                    rs.getInt("ISVACATION") == 1
                );
                periods.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periods;
    }

    public boolean delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");

        String sql = "DELETE FROM Period WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
