package ex1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JDBCAddressDaoImpl addressDao = new JDBCAddressDaoImpl();
        JDBCPersonDaoImpl personDao = new JDBCPersonDaoImpl();
        JDBCCreditCardDaoImpl creditCardDao = new JDBCCreditCardDaoImpl();

        System.out.println("=== Deleting All Data ===\n");
        personDao.deleteAll();
        addressDao.deleteAll();
        creditCardDao.deleteAll();

        System.out.println("=== Adding New Addresses ===\n");
        Set<Address> addresses = new HashSet<Address>();

        Address address1 = new Address("Iasi", "Principala", "Romania");
        addressDao.insert(address1);
        Address address2 = new Address("Cluj", "Republicii", "Romania");
        addressDao.insert(address2);
        Address address3 = new Address("Bucuresti", "Viitorului", "Romania");
        addressDao.insert(address3);
        addresses = addressDao.findAll();
        addresses.forEach(System.out::println);

        System.out.println("\n=== Adding New People ===\n");
        ArrayList<Person> people = new ArrayList<>();

        Person person1 = new Person("John Doe", address1, LocalDate.of(2000, 3, 2), "Pilot");
        personDao.insert(person1);
        Person person2 = new Person("Jane Smith", address2, LocalDate.of(2000, 10, 12), "Programmer");
        personDao.insert(person2);
        Person person3 = new Person("Lalo Salamanca", address3, LocalDate.of(1975, 6, 9), "CEO");
        personDao.insert(person3);
        Person person4 = new Person("Anna Ryder", address1, LocalDate.of(1982, 7, 20), "Fire fighter");
        personDao.insert(person4);
        Person person5 = new Person("Jack Smith", address2, LocalDate.of(2001, 11, 14), "Police Officer");
        personDao.insert(person5);
        people = personDao.findAll();
        people.forEach(System.out::println);

        System.out.println("\n=== Adding New Cards ===\n");
        List<CreditCard> cards = new ArrayList<>();
        CreditCard creditCard1 = new CreditCard("1111-2222-3333-4444", 501.5, person1.getId());
        creditCardDao.insert(creditCard1);
        CreditCard creditCard2 = new CreditCard("2222-3333-4444-5555", 410.95, person2.getId());
        creditCardDao.insert(creditCard2);
        CreditCard creditCard3 = new CreditCard("3333-4444-5555-6666", 920.00, person3.getId());
        creditCardDao.insert(creditCard3);
        CreditCard creditCard4 = new CreditCard("4444-5555-6666-7777", 80.00, person4.getId());
        creditCardDao.insert(creditCard4);
        CreditCard creditCard5 = new CreditCard("5555-6666-7777-8888", 8001.01, person5.getId());
        creditCardDao.insert(creditCard5);
        CreditCard creditCard6 = new CreditCard("6666-7777-8888-9999", 1.01, person1.getId());
        creditCardDao.insert(creditCard6);
        CreditCard creditCard7 = new CreditCard("7777-8888-8888-9999", 100000.001, person2.getId());
        creditCardDao.insert(creditCard7);
        cards = creditCardDao.findAll();
        cards.forEach(System.out::println);

        System.out.println("\n=== Allowing the user to insert another Person object with a new Address and a new CreditCard\n" +
                "in the database from the Java application. ===\n");

        Scanner scanner = new Scanner(System.in);

        // Prompt for person details
        System.out.print("Enter the new person's name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter the new person's birth date (YYYY-MM-DD): ");
        String birthDateStr = scanner.nextLine();
        LocalDate newBirthDate = LocalDate.parse(birthDateStr);
        System.out.print("Enter the new person's job: ");
        String newJob = scanner.nextLine();

        // Prompt for address details
        System.out.print("Enter the new address's city: ");
        String newCity = scanner.nextLine();
        System.out.print("Enter the new address's street: ");
        String newStreet = scanner.nextLine();
        System.out.print("Enter the new address's country: ");
        String newCountry = scanner.nextLine();
        Address newAddress = new Address(newCity, newStreet, newCountry);
        addressDao.insert(newAddress);

        // Create and insert the new person
        Person newPerson = new Person(newName, newAddress, newBirthDate, newJob);
        personDao.insert(newPerson);

        // Prompt for credit card details for the new person
        System.out.print("Enter the new credit card IBAN: ");
        String newIban = scanner.nextLine();
        System.out.print("Enter the new credit card amount: ");
        double newAmount = Double.parseDouble(scanner.nextLine());
        CreditCard newCreditCard = new CreditCard(newIban, newAmount, newPerson.getId());
        creditCardDao.insert(newCreditCard);

        // Display updated list of people, addresses, and credit cards
        System.out.println("\n=== Updated List of People, Addresses, and Credit Cards ===\n");
        people = personDao.findAll();
        addresses = addressDao.findAll();
        cards = creditCardDao.findAll();
        people.forEach(System.out::println);
        System.out.println();
        addresses.forEach(System.out::println);
        System.out.println();
        cards.forEach(System.out::println);

        addressDao.closeConnection();
        personDao.closeConnection();
        creditCardDao.closeConnection();
    }
}


