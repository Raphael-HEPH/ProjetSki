package be.couder.ecoleski.Folder_JPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import be.couder.ecoledeski.Booking;
import be.couder.ecoledeski.Instructor;
import be.couder.ecoledeski.Lesson;
import be.couder.ecoledeski.Period;
import be.couder.ecoledeski.Student;
import be.couder.ecoledeski.ConnectToDB;

public class BookingFormPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton createButton, deleteButton, searchButton, viewAllButton;

    public BookingFormPanel() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        ensureConnection();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        createButton = new JButton("Créer une réservation");
        createButton.addActionListener(e -> openCreateBookingDialog());

        deleteButton = new JButton("Supprimer une réservation");
        deleteButton.addActionListener(e -> openDeleteBookingDialog());

        searchButton = new JButton("Rechercher une réservation");
        searchButton.addActionListener(e -> openSearchBookingDialog());

        viewAllButton = new JButton("Voir toutes les réservations");
        viewAllButton.addActionListener(e -> openViewAllBookingsDialog());

        panel.add(createButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(viewAllButton);

        return panel;
    }

    private void openCreateBookingDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Créer une réservation", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        List<Student> students = Student.getAllStudent();
        List<Instructor> instructors = Instructor.getAllInstructors();
        List<Lesson> lessons = Lesson.getAllLessons();
        List<Period> periods = Period.getAllPeriods();

        JComboBox<Student> studentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        JComboBox<Instructor> instructorComboBox = new JComboBox<>(instructors.toArray(new Instructor[0]));
        JComboBox<Lesson> lessonComboBox = new JComboBox<>(lessons.toArray(new Lesson[0]));
        JComboBox<Period> periodComboBox = new JComboBox<>(periods.toArray(new Period[0]));
        JCheckBox insuranceCheckBox = new JCheckBox("Assurance");
        JCheckBox discountCheckBox = new JCheckBox("Réduction");

        panel.add(new JLabel("Étudiant:")); panel.add(studentComboBox);
        panel.add(new JLabel("Instructeur:")); panel.add(instructorComboBox);
        panel.add(new JLabel("Leçon:")); panel.add(lessonComboBox);
        panel.add(new JLabel("Période:")); panel.add(periodComboBox);
        panel.add(new JLabel("Options:")); panel.add(insuranceCheckBox);
        panel.add(new JLabel("")); panel.add(discountCheckBox);

        JButton submit = new JButton("Créer");
        submit.addActionListener(e -> {
            Student student = (Student) studentComboBox.getSelectedItem();
            Instructor instructor = (Instructor) instructorComboBox.getSelectedItem();
            Lesson lesson = (Lesson) lessonComboBox.getSelectedItem();
            Period period = (Period) periodComboBox.getSelectedItem();
            boolean hasInsurance = insuranceCheckBox.isSelected();
            boolean hasDiscount = discountCheckBox.isSelected();

            if (student == null || instructor == null || lesson == null || period == null) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            Booking booking = new Booking(0, hasInsurance, hasDiscount, lesson, instructor, student, period);
            if (booking.addBooking()) {
                JOptionPane.showMessageDialog(dialog, "Réservation créée avec succès !");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Échec de la création de la réservation.");
            }
        });

        panel.add(submit);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openDeleteBookingDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Supprimer une réservation", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        List<Booking> bookings = Booking.getAllBookings();
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Aucune réservation à supprimer.");
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Booking b : bookings) {
            model.addElement(b.getId() + " - " + b.getStudent().getFirstName() + " " + b.getStudent().getLastName());
        }

        JComboBox<String> bookingComboBox = new JComboBox<>(model);
        JButton deleteBtn = new JButton("Supprimer");
        deleteBtn.addActionListener(e -> {
            String selected = (String) bookingComboBox.getSelectedItem();
            if (selected != null) {
                int id = Integer.parseInt(selected.split(" - ")[0]);
                Booking booking = Booking.getBookingById(id);
                boolean deleted = booking.deleteBooking();
                JOptionPane.showMessageDialog(dialog,
                        deleted ? "Réservation supprimée avec succès !" : "Échec de la suppression.");
                dialog.dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(bookingComboBox);
        panel.add(deleteBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openSearchBookingDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Rechercher une réservation", true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField idField = new JTextField();

        panel.add(new JLabel("ID de la réservation:"));
        panel.add(idField);

        JButton searchBtn = new JButton("Rechercher");
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Booking booking = Booking.getBookingById(id);
                if (booking != null) {
                    JOptionPane.showMessageDialog(dialog,
                            "Réservation trouvée:\n" +
                                    "Étudiant: " + booking.getStudent().getFirstName() + " " + booking.getStudent().getLastName() + "\n" +
                                    "Instructeur: " + booking.getInstructor().getFirstName() + " " + booking.getInstructor().getLastName() + "\n" +
                                    "Leçon: " + booking.getLesson().getLessonType().getName() + "\n" +
                                    "Période: " + booking.getPeriod().getStartDate() + " - " + booking.getPeriod().getEndDate() + "\n" +
                                    "Assurance: " + booking.isHasInsurance() + "\n" +
                                    "Réduction: " + booking.isHasDiscount() + "\n" +
                                    "Prix total: " + booking.getTotalPrice()
                    );
                } else {
                    JOptionPane.showMessageDialog(dialog, "Aucune réservation trouvée avec cet ID.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer un ID valide.");
            }
            dialog.dispose();
        });

        panel.add(searchBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openViewAllBookingsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Liste des réservations", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        List<Booking> bookings = Booking.getAllBookings();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Booking b : bookings) {
            String lessonName = "Inconnu";
            if (b.getLesson() != null && b.getLesson().getLessonType() != null) {
                lessonName = b.getLesson().getLessonType().getName();
                if (b.getLesson().getLessonType().getAccreditation() != null) {
                    lessonName += " (" + b.getLesson().getLessonType().getAccreditation().getName() + ")";
                }
            }

            listModel.addElement(b.getId() + " - " + b.getStudent().getFirstName() + " " + b.getStudent().getLastName() +
                    " | " + lessonName +
                    " | " + b.getInstructor().getFirstName());
        }

        JList<String> bookingJList = new JList<>(listModel);
        bookingJList.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(bookingJList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void ensureConnection() {
        ConnectToDB.getInstance();
    }
}
