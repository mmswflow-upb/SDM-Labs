import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Representation {
    private Date date;
    private Hall hall; // Directed association (1)
    private List<Singer> cast; // cast relationship (1..* to 0..*)
    private List<Ticket> tickets; // Directed association (1)

    public Representation(Date date, Hall hall) {
        this.date = date;
        this.hall = hall;
        this.cast = new ArrayList<>();
        this.tickets = new ArrayList<>();
    }
}