package be.couder.ecoledeski;

import java.util.ArrayList;
import java.util.List;

import be.couder.ecoleski.DAO_Folder.AccreditationDAO;
import be.couder.ecoleski.DAO_Folder.LessonDAO;
import be.couder.ecoleski.DAO_Folder.LessonTypeDAO;

public class LessonType {

    private int id;
    private double price;
    private String name;
    private int lessonLevel;
    private Accreditation accreditation;
    private List<Lesson> lessons;

    
    private static final LessonTypeDAO lessonTypeDAO = new LessonTypeDAO();
    private static final AccreditationDAO accreditationDAO = new AccreditationDAO();
    private static final LessonDAO lessonDAO = new LessonDAO();

    public LessonType() {
        lessons = new ArrayList<>();
    }

    public LessonType(int id, String name, double price, int lessonLevel, Accreditation accreditation) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.lessonLevel = lessonLevel;
        lessons = new ArrayList<>();
        setAccreditation(accreditation);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Le prix doit être positif.");
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide.");
        this.name = name;
    }

    public int getLessonLevel() { return lessonLevel; }
    public void setLessonLevel(int lessonLevel) { this.lessonLevel = lessonLevel; }

    public Accreditation getAccreditation() { return accreditation; }
    public void setAccreditation(Accreditation accreditation) {
        this.accreditation = accreditation;
        if (accreditation != null && !accreditation.getLessonTypes().contains(this)) {
            accreditation.addLessonType(this);
        }
    }

    public int getAccreditationId() {
        return (accreditation != null) ? accreditation.getId() : -1;
    }

    public void addLesson(Lesson lesson) {
        if (lesson != null && !lessons.contains(lesson)) {
            lessons.add(lesson);
            if (lesson.getLessonType() != this) {
                lesson.setLessonType(this);
            }
        }
    }

    public void chargerRelations() {
        if (id <= 0) throw new IllegalArgumentException("Id doit être plus grand que 0.");

        Accreditation acc = accreditationDAO.getAccreditationByLessonTypeId(this.id);
        if (acc != null) setAccreditation(acc);

        List<Lesson> lessonsFromDB = lessonDAO.getLessonsByLessonTypeId(this.id);
        for (Lesson lesson : lessonsFromDB) addLesson(lesson);
    }

    public boolean addLessonType() {
        return lessonTypeDAO.create(this);
    }

    public boolean deleteLessonTypeById() {
        if (id <= 0) throw new IllegalArgumentException("Id plus petit ou égal à 0");
        return lessonTypeDAO.delete(id);
    }

    public static LessonType getLessonTypeById(int id) {
        LessonType lessonType = lessonTypeDAO.getById(id);
        if (lessonType != null) lessonType.chargerRelations();
        return lessonType;
    }

    public static List<LessonType> getAllLessonTypes() {
        List<LessonType> lessonTypes = lessonTypeDAO.getAll();
        for (LessonType lt : lessonTypes) lt.chargerRelations();
        return lessonTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonType that = (LessonType) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public String toString() {
        return "LessonType : id=" + id + " nom=" + name + " prix=" + price;
    }
}
