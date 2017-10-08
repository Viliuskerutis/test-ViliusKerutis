import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataClass {

    private BasicDataSource dataSource;

    public DataClass() {
        dataSource = new BasicDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testas?useUnicode=yes&characterEncoding=UTF-8");
        dataSource.setValidationQuery("SELECT 1");
    }


    public void insert(String vardas, String pavarde, String email, String adresas) {
        String sql = "INSERT  INTO vartotojai " + "(vardas, pavarde, email, adresas) " + "VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vardas);
            statement.setString(2, pavarde);
            statement.setString(3, email);
            statement.setString(4, adresas);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Įvyko klaida: " + e.getLocalizedMessage());
        }
    }

    public String select(String vardas, String pavarde, String email) {
        String sql = "SELECT * FROM vartotojai WHERE vardas = ? OR pavarde = ? OR email = ?";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, vardas);
            statement.setString(2, pavarde);
            statement.setString(3, email);
            ResultSet resultSet = statement.executeQuery();
            String users = "";
            while (resultSet.next()) {
                users = users + " " + resultSet.getString(1) + " " + resultSet.getString(2)
                        + " " + resultSet.getString(3) + " " + resultSet.getString(4) + " <br/>";
            }
            return users;
        } catch (SQLException e) {
            System.out.println("Įvyko klaida: " + e.getLocalizedMessage());
        }
        return "Nėra vartotojų su tokiu vardu";
    }
}
