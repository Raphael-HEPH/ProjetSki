package be.couder.ecoleski.Folder_JPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import be.couder.ecoledeski.ConnectToDB;
import be.couder.ecoledeski.Instructor;
import be.couder.ecoledeski.Accreditation;

public class InstructorFormPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton submitButton, deleteButton, searchButton, viewInstructorsButton;

    public InstructorFormPanel() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        ensureConnection();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        submitButton = new JButton("Create Instructor");
        submitButton.addActionListener(e -> openCreateInstructorDialog());

        deleteButton = new JButton("Delete Instructor");
        deleteButton.addActionListener(e -> openDeleteInstructorDialog());

        searchButton = new JButton("Search Instructor");
        searchButton.addActionListener(e -> openSearchInstructorDialog());

        viewInstructorsButton = new JButton("View All Instructors");
        viewInstructorsButton.addActionListener(e -> openViewInstructorsDialog());

        panel.add(submitButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(viewInstructorsButton);

        return panel;
    }

    private void openCreateInstructorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Create Instructor", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

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

        List<Accreditation> accreditations = Accreditation.getAllAccreditations();
        JPanel accreditationPanel = new JPanel(new GridLayout(accreditations.size(), 1));
        List<JCheckBox> accreditationCheckboxes = new ArrayList<>();
        for (Accreditation acc : accreditations) {
            JCheckBox checkBox = new JCheckBox(acc.getName());
            accreditationCheckboxes.add(checkBox);
            accreditationPanel.add(checkBox);
        }
        panel.add(new JLabel("Accreditations:"));
        panel.add(accreditationPanel);

        JButton submit = new JButton("Create Instructor");
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

                List<Accreditation> selectedAccreditations = new ArrayList<>();
                for (int i = 0; i < accreditations.size(); i++) {
                    if (accreditationCheckboxes.get(i).isSelected()) {
                        selectedAccreditations.add(accreditations.get(i));
                    }
                }

                if (selectedAccreditations.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please select at least one accreditation.");
                    return;
                }

                Accreditation firstAcc = selectedAccreditations.get(0);
                Instructor instructor = new Instructor(0, firstName, lastName, age, email, phone, address, firstAcc);

                if (instructor.addInstructor()) {
                    for (int i = 1; i < selectedAccreditations.size(); i++) {
                        instructor.addAccreditation(selectedAccreditations.get(i));
                    }

                    JOptionPane.showMessageDialog(dialog, "Instructor created successfully!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to create instructor.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid age.");
            }
        });

        panel.add(submit);
        dialog.add(panel);
        dialog.setVisible(true);
    }



    private void openDeleteInstructorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Delete Instructor", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        List<Instructor> instructors = Instructor.getAllInstructors(); 

        if (instructors.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "No instructors to delete.");
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Instructor i : instructors) {
            model.addElement(i.getId() + " - " + i.getFirstName() + " " + i.getLastName());
        }

        JComboBox<String> instructorComboBox = new JComboBox<>(model);
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Select instructor to delete:"));
        panel.add(instructorComboBox);

        JButton deleteBtn = new JButton("Delete Instructor");
        deleteBtn.addActionListener(e -> {
            String selected = (String) instructorComboBox.getSelectedItem();
            if (selected != null) {
                int id = Integer.parseInt(selected.split(" - ")[0]);
                boolean deleted = new Instructor().deleteInstructorById(id); 
                JOptionPane.showMessageDialog(dialog,
                        deleted ? "Instructor deleted successfully!" : "No instructor found with this ID.");
                dialog.dispose();
            }
        });

        panel.add(deleteBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openSearchInstructorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Search Instructor", true);
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

            Instructor instructor = Instructor.getAllInstructors().stream()
                    .filter(i -> i.getFirstName().equals(firstName) && i.getLastName().equals(lastName))
                    .findFirst().orElse(null);

            if (instructor != null) {
                JOptionPane.showMessageDialog(dialog,
                        "Instructor found:\nName: " + instructor.getFirstName() + " " + instructor.getLastName() +
                                "\nAge: " + instructor.getAge() +
                                "\nEmail: " + instructor.getEmail() +
                                "\nPhone: " + instructor.getPhone() +
                                "\nAddress: " + instructor.getAddress() +
                                "\nAccreditation: " + instructor.getAccreditations());
            } else {
                JOptionPane.showMessageDialog(dialog, "No instructor found with the provided name.");
            }
            dialog.dispose();
        });

        panel.add(searchBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openViewInstructorsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Instructors List", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        List<Instructor> instructors = Instructor.getAllInstructors(); 

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Instructor i : instructors) listModel.addElement(i.getId() + " - " + i.getFirstName() + " " + i.getLastName());

        JList<String> instructorJList = new JList<>(listModel);
        instructorJList.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(instructorJList);
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
