import java.util.Date;

public class Singer extends Person {
    private String vocalClassification;

    public Singer(String name, Date dateOfBirth, char gender, String vocalClassification) {
        super(name, dateOfBirth, gender);
        this.vocalClassification = vocalClassification;
    }
}