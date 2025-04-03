package ex1;

import java.util.Objects;

public class Address {
    private int id;
    private String city,street, country;

    public Address(String city, String street, String country) {
        this.city = city;
        this.street = street;
        this.country = country;
    }

    public Address(int id, String city, String street, String country) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id || Objects.equals(city, address.city) || Objects.equals(street, address.street) || Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setId(int id) {
        this.id = id;
    }
}
