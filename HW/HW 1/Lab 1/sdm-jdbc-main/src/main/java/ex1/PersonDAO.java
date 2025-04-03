package ex1;

import java.util.ArrayList;
import java.util.Set;

public interface PersonDAO {
    ArrayList<Person> findAll();
    Set<Person> findByCity(String city);
    Person insert(Person Person);
    void update(Person Person);
    void delete(int PersonId);
    void deleteAll();
}
