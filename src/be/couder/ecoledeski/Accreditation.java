package be.couder.ecoledeski;

import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.AccreditationDAO;
import be.couder.ecoleski.DAO_Folder.InstructorDAO;
import be.couder.ecoleski.DAO_Folder.LessonTypeDAO;

public class Accreditation {
    
    private int id;
    private String name;
    private List<Instructor> instructors;
    private List<LessonType> lessonTypes;

    private static final AccreditationDAO accreditationDAO = new AccreditationDAO();
    private static final InstructorDAO instructorDAO = new InstructorDAO();
    private static final LessonTypeDAO lessonTypeDAO = new LessonTypeDAO();
    
    public Accreditation(int id, String name) {
        this.id = id;
        this.name = name;
        this.instructors = new ArrayList<>();
        this.lessonTypes = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Instructor> getInstructors() { return instructors; }
    public void setInstructors(List<Instructor> instructors) { this.instructors = instructors; }

    public List<LessonType> getLessonTypes() { return lessonTypes; }
    public void setLessonTypes(List<LessonType> lessonTypes) { this.lessonTypes = lessonTypes; }

    public boolean addAccreditation() {
        return accreditationDAO.create(this);
    }

    public boolean deleteAccreditationById(int id) {
        if(id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");
        return accreditationDAO.delete(id);
    }

    public void chargerRelations() {
        if(id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0, impossible de charger les relations");

        this.instructors = instructorDAO.getInstructorsByAccreditationId(this.id);
        for (Instructor instructor : instructors) {
            instructor.addAccreditation(this);
        }

        this.lessonTypes = lessonTypeDAO.getLessonTypesByAccreditationId(this.id);
        for (LessonType lt : lessonTypes) {
            lt.setAccreditation(this);
        }
    }

    public static Accreditation getAccreditationById(int id) {
        if(id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");
        Accreditation accreditation = accreditationDAO.getById(id);
        if (accreditation != null) {
            accreditation.chargerRelations();
        }
        return accreditation;
    }

    public static List<Accreditation> getAllAccreditations() {
        List<Accreditation> accs = accreditationDAO.getAll();
        for (Accreditation acc : accs) {
            acc.chargerRelations();
        }
        return accs;
    }

    public void addInstructor(Instructor instructor) {
        if (instructor != null && !instructors.contains(instructor)) {
            instructors.add(instructor);
            if (!instructor.getAccreditations().contains(this)) {
                instructor.addAccreditation(this);
            }
        }
    }

    public void removeInstructor(Instructor instructor) {
        if (instructor != null && instructors.contains(instructor)) {
            instructors.remove(instructor);
            instructor.getAccreditations().remove(this);
        }
    }

    public void addLessonType(LessonType lessonType) {
        if (lessonType != null && !lessonTypes.contains(lessonType)) {
            lessonTypes.add(lessonType);
            lessonType.setAccreditation(this);
        }
    }

    public void removeLessonType(LessonType lessonType) {
        if (lessonType != null && lessonTypes.contains(lessonType)) {
            lessonTypes.remove(lessonType);
            lessonType.setAccreditation(null);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Accreditation that = (Accreditation) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
