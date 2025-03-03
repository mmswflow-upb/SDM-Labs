package ex2;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        JDBCAddressDaoImpl addressDao = new JDBCAddressDaoImpl();
        JDBCPersonDaoImpl personDao = new JDBCPersonDaoImpl();

        System.out.println("=== Deleting All Data ===");
        personDao.deleteAll();
        addressDao.deleteAll();

        System.out.println("=== Adding New Address ===");
        Set<Address> addresses = new HashSet<Address>();

        Address address1 = new Address("Iasi", "Principala");
        addressDao.insert(address1);
        Address address2 = new Address("Cluj", "Republicii");
        addressDao.insert(address2);
        addresses = addressDao.findAll();
        addresses.forEach(System.out::println);

        System.out.println("=== Adding New People ===");
        Person person1 = new Person("John Doe", address1);
        personDao.insert(person1);
        Person person2 = new Person("Jane Smith", address2);
        personDao.insert(person2);


        System.out.println("=== Updating Person ===");

        Set<Person> people = new HashSet<Person>();

        person1.setName("John Updated");
        personDao.update(person1);
        people = personDao.findAll();
        people.forEach(System.out::println);

        Address duplicateAddress = new Address("Iasi", "Principala");
        System.out.println("=== Trying to Insert Duplicate Address === || " + duplicateAddress);

        addressDao.insert(duplicateAddress);
        addresses = addressDao.findAll();
        addresses.forEach(System.out::println);

        System.out.println("=== Deleting a Person ===");
        personDao.delete(person2.getId());
        people = personDao.findAll();
        people.forEach(System.out::println);

        addressDao.closeConnection();
        personDao.closeConnection();
    }
}


