package ex2;

import java.util.Optional;
import java.util.Set;

public interface AddressDAO {
    Set<Address> findAll();
    Set<Address> findByCity(String city);
    Address insert(Address address);
    void update(Address address);
    void delete(int addressId);
    void deleteAll();

}




