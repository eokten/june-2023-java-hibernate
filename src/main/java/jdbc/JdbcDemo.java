package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcDemo {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:postgresql://localhost:54320/postgres";
        String user = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            boolean connected = connection.isValid(5);
            System.out.println("Connected: " + connected);

            Statement statement = connection.createStatement();
            statement.execute("create table if not exists products (id bigint, name varchar(255))");

            String productName = "test";
            statement.execute("insert into products (id, name) values (1, '" + productName + "')");

            executeQueriesWithSafety(connection);

            String vulnerableProductName = "asd'; delete from products where name='%s";
            ResultSet resultSet = statement.executeQuery("select * from products where name='" + vulnerableProductName + "'");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                System.out.println("id: " + id + ", name: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeQueriesWithSafety(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into products (id, name) values (?, ?)")) {
            preparedStatement.setLong(1, 100);
            preparedStatement.setString(2, "'); delete products");

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
