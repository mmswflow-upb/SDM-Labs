package ex2;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JDBCPersonDaoImpl extends CoreJDBCDao implements PersonDAO {
    @Override
    public Set<Person> findAll() {
        Set<Person> people = new HashSet<>();
        String sql = "SELECT p.id, p.name, a.id as id, a.city, a.street FROM persons p " +
                "JOIN addresses a ON p.id = a.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Address address = new Address(rs.getInt("id"), rs.getString("city"), rs.getString("street"));
                Person person = new Person(rs.getInt("id"), rs.getString("name"), address);
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    @Override
    public Set<Person> findByCity(String city) {
        Set<Person> people = new HashSet<>();
        String sql = "SELECT p.id, p.name, a.id as id, a.city, a.street FROM persons p " +
                "JOIN addresses a ON p.id = a.id WHERE a.city = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Address address = new Address(rs.getInt("id"), rs.getString("city"), rs.getString("street"));
                Person person = new Person(rs.getInt("id"), rs.getString("name"), address);
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    @Override
    public Person insert(Person person) {
        int addressId = getOrInsertAddress(person.getAddress());
        String sql = "INSERT INTO persons(name, id) VALUES(?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getName());
            stmt.setInt(2, addressId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                person.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    private int getOrInsertAddress(Address address) {
        String selectSQL = "SELECT id FROM addresses WHERE city = ? AND street = ?";
        String insertSQL = "INSERT INTO addresses(city, street) VALUES(?, ?)";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
            selectStmt.setString(1, address.getCity());
            selectStmt.setString(2, address.getStreet());
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, address.getCity());
            insertStmt.setString(2, address.getStreet());
            insertStmt.executeUpdate();
            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(Person person) {
        int addressId = getOrInsertAddress(person.getAddress());
        String sql = "UPDATE persons SET name = ?, id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            stmt.setInt(2, addressId);
            stmt.setInt(3, person.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int personId) {
        String sql = "DELETE FROM persons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, personId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll(){
        String sql = "DELETE FROM persons";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
