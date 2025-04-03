package ex1;

import java.sql.*;

public class Main {
  public static void main(String[] args) {
    String url = "jdbc:mariadb://localhost:3306/jdbcex";
    String city = "Bucuresti";
    String getPersonsFromCitySQL = "SELECT p.name FROM persons p"
            + " JOIN addresses a ON p.address=a.id WHERE a.city = ?";

    try (
            Connection con = DriverManager.getConnection(url, "root", "root");

            Statement instr = con.createStatement();
            PreparedStatement getPersonsFromCity = con.prepareStatement(getPersonsFromCitySQL);
    ) {
      getPersonsFromCity.setString(1, city);
      ResultSet rs = getPersonsFromCity.executeQuery();
      String sql = "SELECT * FROM persons";
      ResultSet rs2 = instr.executeQuery(sql);

      while (rs.next())
        System.out.println(rs.getString("name"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
