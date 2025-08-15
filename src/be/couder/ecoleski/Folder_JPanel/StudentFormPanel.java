package be.couder.ecoleski.Folder_JPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Student;

public class StudentFormPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton submitButton, deleteButton, searchButton, viewStudentsButton;

    public StudentFormPanel() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        ensureConnection();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        submitButton = new JButton("Create Student");
        submitButton.addActionListener(e -> openCreateStudentDialog());

        deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> openDeleteStudentDialog());

        searchButton = new JButton("Search Student");
        searchButton.addActionListener(e -> openSearchStudentDialog());

        viewStudentsButton = new JButton("View All Students");
        viewStudentsButton.addActionListener(e -> openViewStudentsDialog());

        panel.add(submitButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(viewStudentsButton);

        return panel;
    }

    private void openCreateStudentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Create Student", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));  panel.add(lastNameField);
        panel.add(new JLabel("Age:"));        panel.add(ageField);
        panel.add(new JLabel("Email:"));      panel.add(emailField);
        panel.add(new JLabel("Phone:"));      panel.add(phoneField);
        panel.add(new JLabel("Address:"));    panel.add(addressField);

        JButton submit = new JButton("Create Student");
        submit.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.");
                    return;
                }

                Student student = new Student(0, firstName, lastName, age, email, phone, address);
                if (student.addStudent()) { // plus de paramètre DAO
                    JOptionPane.showMessageDialog(dialog, "Student created successfully!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to create student.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid age.");
            }
        });

        panel.add(submit);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openDeleteStudentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Delete Student", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        List<Student> students = Student.getAllStudent(); 

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Aucun étudiant à supprimer.");
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Student s : students) {
            model.addElement(s.getId() + " - " + s.getFirstName() + " " + s.getLastName());
        }

        JComboBox<String> studentComboBox = new JComboBox<>(model);
        panel.add(new JLabel("Sélectionnez l'étudiant à supprimer :"));
        panel.add(studentComboBox);

        JButton deleteBtn = new JButton("Supprimer l'étudiant");
        deleteBtn.addActionListener(e -> {
            String selected = (String) studentComboBox.getSelectedItem();
            if (selected != null) {
                int id = Integer.parseInt(selected.split(" - ")[0]);

                boolean deleted = new Student().deleteStudentById(id); 

                JOptionPane.showMessageDialog(dialog,
                        deleted ? "Étudiant supprimé avec succès !" : "Aucun étudiant trouvé avec cet ID.");
                dialog.dispose();
            }
        });

        panel.add(deleteBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openSearchStudentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Search Student", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));  panel.add(lastNameField);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter both first name and last name.");
                return;
            }
            Student student = Student.getAllStudent().stream()
                    .filter(s -> s.getFirstName().equals(firstName) && s.getLastName().equals(lastName))
                    .findFirst().orElse(null);

            if (student != null) {
                JOptionPane.showMessageDialog(dialog,
                        "Student found:\nName: " + student.getFirstName() + " " + student.getLastName() +
                                "\nAge: " + student.getAge() +
                                "\nEmail: " + student.getEmail() +
                                "\nPhone: " + student.getPhone() +
                                "\nAddress: " + student.getAddress());
            } else {
                JOptionPane.showMessageDialog(dialog, "No student found with the provided name.");
            }
            dialog.dispose();
        });

        panel.add(searchBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openViewStudentsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Students List", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        List<Student> students = Student.getAllStudent(); 

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Student s : students) listModel.addElement(s.getId() + " - " + s.getFirstName() + " " + s.getLastName());

        JList<String> studentJList = new JList<>(listModel);
        studentJList.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(studentJList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void ensureConnection() {
        ConnectToDB.getInstance();
    }
}
