import java.util.Date;

public class Person {
    private String name;
    private Date dateOfBirth;
    private char gender;

    public Person(String name, Date dateOfBirth, char gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}