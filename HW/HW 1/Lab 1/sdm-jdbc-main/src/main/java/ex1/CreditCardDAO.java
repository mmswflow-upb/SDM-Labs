package ex1;

import java.util.List;

public interface CreditCardDAO {
    /**
     * Inserts a new CreditCard record into the database.
     * The CreditCard object's id will be updated with the generated key.
     *
     * @param cc the CreditCard object to insert
     */
    void insert(CreditCard cc);

    /**
     * Retrieves a list of CreditCard records that belong to the specified person.
     *
     * @param personId the id of the person
     * @return a List of CreditCard objects for that person
     */
    List<CreditCard> findByPersonId(int personId);

    /**
     * Retrieves all CreditCard records from the database.
     *
     * @return a List containing all CreditCard objects.
     */
    List<CreditCard> findAll();

    /**
     * Deletes all CreditCard records from the database.
     */
    void deleteAll();
}
