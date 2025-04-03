package ex2;

import java.util.Optional;
import java.util.Set;

public interface PersonDAO {
    Set<Person> findAll();
    Set<Person> findByCity(String city);
    Person insert(Person Person);
    void update(Person Person);
    void delete(int PersonId);
    void deleteAll();
}
