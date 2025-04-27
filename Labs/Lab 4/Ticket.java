public class Ticket {
    private double price;
    private String code;
    private Seat seat; // Directed association (1)
    private Representation representation; // Directed association (1)

    public Ticket(double price, String code, Seat seat, Representation representation) {
        this.price = price;
        this.code = code;
        this.seat = seat;
        this.representation = representation;
    }
}