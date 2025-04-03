package ex1;

public class CreditCard {
    private String IBAN;
    private double amount;
    private int id, personId;

    public CreditCard(int id, String IBAN, double amount, int personId) {
        this.IBAN = IBAN;
        this.amount = amount;
        this.personId = personId;
        this.id = id;
    }

    public CreditCard(String IBAN, double amount, int personId) {
        this.IBAN = IBAN;
        this.amount = amount;
        this.personId = personId;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public double getAmount() {
        return amount;
    }

    public int getPersonId() {
        return personId;
    }

    public int getId() {
        return id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", IBAN='" + IBAN + '\'' +
                ", amount=" + amount +
                ", personId=" + personId +
                '}';
    }

    public void setId(int anInt) {
        id = anInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditCard that = (CreditCard) o;

        if (id != that.id) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (personId != that.personId) return false;
        return IBAN != null ? IBAN.equals(that.IBAN) : that.IBAN == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (IBAN != null ? IBAN.hashCode() : 0);
        long temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + personId;
        return result;
    }

}
