package be.couder.ecoleski.Folder_JPanel;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        setTitle("École de Ski - Menu Principal");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel titleLabel = new JLabel("Ecole de ski", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Bienvenue ! Sélectionnez une section à gauche.", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        cardPanel.add(welcomePanel, "welcome");

        JPanel studentFormPanel = new StudentFormPanel();
        JPanel instructorFormPanel = new InstructorFormPanel();
        JPanel bookingFormPanel = new BookingFormPanel();
        JPanel lessonFormPanel = new LessonFormPanel();

        cardPanel.add(studentFormPanel, "student");
        cardPanel.add(instructorFormPanel, "instructor");
        cardPanel.add(bookingFormPanel, "bookings");
        cardPanel.add(lessonFormPanel, "lesson");

        add(cardPanel, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton studentButton = new JButton("Student");
        JButton instructorButton = new JButton("Instructor");
        JButton bookingsButton = new JButton("Bookings");
        JButton lessonButton = new JButton("Lesson");

        studentButton.addActionListener(e -> cardLayout.show(cardPanel, "student"));
        instructorButton.addActionListener(e -> cardLayout.show(cardPanel, "instructor"));
        bookingsButton.addActionListener(e -> cardLayout.show(cardPanel, "bookings"));
        lessonButton.addActionListener(e -> cardLayout.show(cardPanel, "lesson"));

        menuPanel.add(studentButton);
        menuPanel.add(instructorButton);
        menuPanel.add(bookingsButton);
        menuPanel.add(lessonButton);

        add(menuPanel, BorderLayout.WEST);

        cardLayout.show(cardPanel, "welcome");

        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
