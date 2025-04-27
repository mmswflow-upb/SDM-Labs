import java.util.List;
import java.util.ArrayList;

public class Hall {
    private int capacity;
    private List<Seat> seats; // Directed association

    public Hall(int capacity) {
        this.capacity = capacity;
        this.seats = new ArrayList<>();
    }
}
