package ex2;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JDBCAddressDaoImpl extends CoreJDBCDao implements AddressDAO{

    @Override
    public Set<Address> findAll() {
        Set<Address> addresses=new HashSet<>();
        String findAllAddressSQL = "SELECT * FROM addresses";
        try (
                PreparedStatement findAllAddress = connection.prepareStatement(findAllAddressSQL);
        ) {
            ResultSet rs = findAllAddress.executeQuery();
            while (rs.next()){
                Address ad=new Address(rs.getInt("id"),rs.getString("city"),rs.getString("street"));
                addresses.add(ad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    public Set<Address> findByCity(String city) {
        Set<Address> addresses = new HashSet<>();
        String findByCitySQL = "SELECT * FROM addresses WHERE city = ?";
        try (PreparedStatement findByCityStmt = connection.prepareStatement(findByCitySQL)) {
            findByCityStmt.setString(1, city);
            ResultSet rs = findByCityStmt.executeQuery();
            while (rs.next()) {
                Address address = new Address(rs.getInt("id"), rs.getString("city"), rs.getString("street"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }


    @Override
    public Address insert(Address address) {

        // Check if the address already exists in the database
        Set<Address> addresses = findByCity(address.getCity());
        for (Address ad : addresses) {
            if (ad.equals(address)) {
                System.out.println("Address already exists in the database");
                return ad;
            }
        }

        String insertAddressSQL = "INSERT into addresses(city,street) values(?,?)";
        try (
           PreparedStatement insertAddress = connection.prepareStatement(insertAddressSQL,Statement.RETURN_GENERATED_KEYS);
        ) {
            insertAddress.setString(1,address.getCity());
            insertAddress.setString(2,address.getStreet());
            insertAddress.executeQuery();
            var generatedKeys = insertAddress.getGeneratedKeys();
            if (generatedKeys.next()) {
                address.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void update(Address address) {
        String updateSQL = "UPDATE addresses SET city = ?, street = ? WHERE id = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
            updateStmt.setString(1, address.getCity());
            updateStmt.setString(2, address.getStreet());
            updateStmt.setInt(3, address.getId());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int addressId) {
        String deleteSQL = "DELETE FROM addresses WHERE id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
            deleteStmt.setInt(1, addressId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll(){
        String sql = "DELETE FROM addresses";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
