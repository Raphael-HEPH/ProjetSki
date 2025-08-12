package be.couder.ecoledeski;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConnectToDB {
    private static Connection instance = null;

    private ConnectToDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@//193.190.64.10:1522/xepdb1";
            String username = "STUDENT03_24";
            String password = "ski2024";

            instance = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Classe de driver introuvable: " + ex.getMessage());
            System.exit(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur JDBC : " + ex.getMessage());
        }
        if (instance == null) {
            JOptionPane.showMessageDialog(null, "La base de données est inaccessible");
            System.exit(0);
        }
    }

    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                new ConnectToDB(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }


    public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getInstance();
    }
}
