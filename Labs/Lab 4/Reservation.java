public class Reservation extends Ticket {
    private double cancelationFee;
    private Person client; // Directional Association (client)

    public Reservation(double price, String code, Seat seat, Representation representation,
            double cancelationFee, Person client) {
        super(price, code, seat, representation);
        this.cancelationFee = cancelationFee;
        this.client = client;
    }
}