import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Opera {
    private Date firstRepresentation;
    private String plot;
    private int numberOfActs;
    private Person composer; // composedBy relationship (1..*)
    private List<Representation> representations;

    public Opera(Date firstRepresentation, String plot, int numberOfActs, Person composer) {
        this.firstRepresentation = firstRepresentation;
        this.plot = plot;
        this.numberOfActs = numberOfActs;
        this.composer = composer;
        this.representations = new ArrayList<>();
    }
}