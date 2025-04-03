package ex1;

import java.time.LocalDate;

public class Person {
    private int id;
    private String name, job;
    private Address address;
    private LocalDate birthDate;

    public Person(String name) {
        this.name = name;
    }

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person(int id, String name, Address address, LocalDate birthDate, String job) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
        this.job = job;
    }

    public Person(String name, Address address, LocalDate birthDate, String job) {
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
        this.job = job;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getJob() {
        return job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", birthDate=" + birthDate +
                ", job='" + job + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != person.id) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (address != null ? !address.equals(person.address) : person.address != null) return false;
        if (job != null ? !job.equals(person.job) : person.job != null) return false;
        return birthDate != null ? birthDate.equals(person.birthDate) : person.birthDate == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }


    public void setId(int id) {
        this.id = id;
    }


}
