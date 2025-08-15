package be.couder.ecoleski.Folder_JPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import be.couder.ecoledeski.Lesson;
import be.couder.ecoledeski.LessonType;
import be.couder.ecoledeski.Accreditation;
import be.couder.ecoledeski.Instructor;
import be.couder.ecoledeski.ConnectToDB;

public class LessonFormPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton createButton, deleteButton, searchButton, viewAllButton;

    public LessonFormPanel() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        ensureConnection();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        createButton = new JButton("Créer une leçon");
        createButton.addActionListener(e -> openCreateLessonDialog());

        deleteButton = new JButton("Supprimer une leçon");
        deleteButton.addActionListener(e -> openDeleteLessonDialog());

        searchButton = new JButton("Rechercher une leçon");
        searchButton.addActionListener(e -> openSearchLessonDialog());

        viewAllButton = new JButton("Voir toutes les leçons");
        viewAllButton.addActionListener(e -> openViewAllLessonsDialog());

        panel.add(createButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(viewAllButton);

        return panel;
    }

    private void openCreateLessonDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Créer une leçon", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));

        JTextField nameField = new JTextField();

        List<Accreditation> accs = Accreditation.getAllAccreditations();
        JComboBox<Accreditation> accComboBox = new JComboBox<>(accs.toArray(new Accreditation[0]));

        List<LessonType> lessonTypes = LessonType.getAllLessonTypes();
        JComboBox<LessonType> lessonTypeComboBox = new JComboBox<>(lessonTypes.toArray(new LessonType[0]));

        JSpinner dateTimeSpinner = new JSpinner(new SpinnerDateModel());
        dateTimeSpinner.setEditor(new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm"));

        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 480, 5));
        JSpinner minStudentSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JSpinner maxStudentSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));

        List<Instructor> instructors = Instructor.getAllInstructors();
        JComboBox<Instructor> instructorComboBox = new JComboBox<>();
        instructorComboBox.addItem(null);
        for (Instructor inst : instructors) instructorComboBox.addItem(inst);

        JCheckBox privateCheckBox = new JCheckBox();

        panel.add(new JLabel("Nom de la leçon:")); panel.add(nameField);
        panel.add(new JLabel("Accréditation:")); panel.add(accComboBox);
        panel.add(new JLabel("Type de leçon:")); panel.add(lessonTypeComboBox);
        panel.add(new JLabel("Date et heure (YYYY-MM-DD HH:mm):")); panel.add(dateTimeSpinner);
        panel.add(new JLabel("Durée (minutes):")); panel.add(durationSpinner);
        panel.add(new JLabel("Min étudiants:")); panel.add(minStudentSpinner);
        panel.add(new JLabel("Max étudiants:")); panel.add(maxStudentSpinner);
        panel.add(new JLabel("Instructeur:")); panel.add(instructorComboBox);
        panel.add(new JLabel("Cours privé:")); panel.add(privateCheckBox);

        JButton submit = new JButton("Créer");
        submit.addActionListener(e -> {
            String lessonName = nameField.getText().trim();
            Accreditation acc = (Accreditation) accComboBox.getSelectedItem();
            LessonType lessonType = (LessonType) lessonTypeComboBox.getSelectedItem();
            java.util.Date dateTimeValue = (java.util.Date) dateTimeSpinner.getValue();
            int duration = (Integer) durationSpinner.getValue();
            int minStudents = (Integer) minStudentSpinner.getValue();
            int maxStudents = (Integer) maxStudentSpinner.getValue();
            Instructor instructor = (Instructor) instructorComboBox.getSelectedItem();
            boolean isPrivate = privateCheckBox.isSelected();

            if (lessonName.isEmpty() || acc == null || lessonType == null) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir le nom, l'accréditation et le type de leçon.");
                return;
            }

            LocalDateTime startHour = dateTimeValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDate lessonDate = startHour.toLocalDate();

            Lesson lesson = new Lesson(0, minStudents, maxStudents, lessonDate, startHour, duration, isPrivate, lessonType, instructor);

            if (lesson.addLesson()) { 
                JOptionPane.showMessageDialog(dialog, "Leçon créée avec succès !");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Échec de la création de la leçon.");
            }
        });

        panel.add(submit);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openDeleteLessonDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Supprimer une leçon", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        List<Lesson> lessons = Lesson.getAllLessons();
        if (lessons.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Aucune leçon à supprimer.");
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Lesson l : lessons) model.addElement(l.getLessonType().getName());

        JComboBox<String> lessonComboBox = new JComboBox<>(model);
        JButton deleteBtn = new JButton("Supprimer");
        deleteBtn.addActionListener(e -> {
            String selected = (String) lessonComboBox.getSelectedItem();
            if (selected != null) {
                Lesson lessonToDelete = lessons.stream()
                        .filter(l -> l.getLessonType().getName().equals(selected))
                        .findFirst().orElse(null);
                if (lessonToDelete != null && Lesson.deleteLessonById(lessonToDelete.getId())) {
                    JOptionPane.showMessageDialog(dialog, "Leçon supprimée avec succès !");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Échec de la suppression.");
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(lessonComboBox);
        panel.add(deleteBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openSearchLessonDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Rechercher une leçon", true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField idField = new JTextField();

        panel.add(new JLabel("ID de la leçon:"));
        panel.add(idField);

        JButton searchBtn = new JButton("Rechercher");
        searchBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer un ID.");
                return;
            }

            try {
                int id = Integer.parseInt(idText);

                Lesson lesson = Lesson.getLessonById(id);
                if (lesson != null) {
                    JOptionPane.showMessageDialog(dialog, "Leçon trouvée: " 
                            + lesson.getLessonType().getName()
                            + " (Accréditation: " 
                            + lesson.getLessonType().getAccreditation().getName() + ")");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Aucune leçon trouvée avec cet ID.");
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(dialog, "L'ID doit être un nombre entier.");
            }

            dialog.dispose();
        });

        panel.add(searchBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openViewAllLessonsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Liste des leçons", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        List<Lesson> lessons = Lesson.getAllLessons();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Lesson l : lessons) {
            LessonType lessonType = l.getLessonType();
            if (lessonType != null) {
                Accreditation accreditation = lessonType.getAccreditation();
                String accName = (accreditation != null) ? accreditation.getName() : "Aucune";
                listModel.addElement(lessonType.getName() + " (" + accName + ")");
            }
        }

        JList<String> lessonJList = new JList<>(listModel);
        lessonJList.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(lessonJList);

        JPanel panel = new JPanel(new BorderLayout());
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
