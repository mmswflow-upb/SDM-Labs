package ex1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCCreditCardDaoImpl extends CoreJDBCDao implements CreditCardDAO {

    /**
     * Inserts a new credit card into the database.
     * The SQL statement inserts the IBAN, amount, and person_id.
     * After execution, the generated key (id) is set in the CreditCard object.
     */
    @Override
    public void insert(CreditCard cc) {
        String insertSQL = "INSERT INTO credit_cards (IBAN, amount, person_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cc.getIBAN());
            stmt.setDouble(2, cc.getAmount());
            stmt.setInt(3, cc.getPersonId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cc.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all credit cards that belong to a person with the given personId.
     * Executes a SELECT query and builds a list of CreditCard objects.
     */
    @Override
    public List<CreditCard> findByPersonId(int personId) {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT id, IBAN, amount, person_id FROM credit_cards WHERE person_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, personId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String IBAN = rs.getString("IBAN");
                double amount = rs.getDouble("amount");
                int pId = rs.getInt("person_id");
                CreditCard cc = new CreditCard(id, IBAN, amount, pId);
                cards.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Retrieves all credit cards from the database.
     * Executes a SELECT query and builds a list of all CreditCard objects.
     */
    @Override
    public List<CreditCard> findAll() {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT id, IBAN, amount, person_id FROM credit_cards";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String IBAN = rs.getString("IBAN");
                double amount = rs.getDouble("amount");
                int pId = rs.getInt("person_id");
                CreditCard cc = new CreditCard(id, IBAN, amount, pId);
                cards.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Deletes all credit card records from the database.
     */
    @Override
    public void deleteAll(){
        String sql = "DELETE FROM credit_cards";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
