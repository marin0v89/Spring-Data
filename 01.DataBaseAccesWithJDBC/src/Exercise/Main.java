package Exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class Main {
    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DATABASE = "minions_db";
    public static BufferedReader reader;
    public static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {

        connection = getConnection();

        System.out.println("Enter exercise index (Starting from 2):");
        String intro = reader.readLine();

        switch (intro) {
            case "2" -> exerciseTwo();
            case "3" -> exerciseThree();

        }


    }

    private static void exerciseThree() throws IOException, SQLException {
        System.out.println("Enter villain ID:");
        String villainID = reader.readLine();

        PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT m.name, m.age FROM minions AS m " +
                        "        JOIN minions_villains mv on m.id = mv.minion_id " +
                        "        JOIN villains v on mv.villain_id = v.id " +
                        "        WHERE v.id = ?;");

        preparedStatement.setString(1, villainID);
        ResultSet resultSet = preparedStatement.executeQuery();

        PreparedStatement villainName = connection.prepareStatement
                ("SELECT name FROM villains WHERE id = ?;");
        villainName.setString(1, villainID);

        ResultSet nameSet = villainName.executeQuery();
        if (nameSet.next()) {
            System.out.println("Villain: " + nameSet.getString(1));
        } else {
            System.out.println("No villain with ID " + villainID + " exists in the database.");
        }
        int idCounter = 0;

        while (resultSet.next()) {

            System.out.printf("%d %s %d%n",
                    ++idCounter, resultSet.getString(1),
                    resultSet.getInt(2));
        }
    }

    private static void exerciseTwo() throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT v.name, COUNT(DISTINCT mv.minion_id) AS minion_count FROM villains AS v " +
                        "JOIN minions_villains AS mv ON v.id = mv.villain_id " +
                        "GROUP BY v.name " +
                        "HAVING minion_count > 15;");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n%n", resultSet.getString("name")
                    , resultSet.getInt(2));
        }
    }

    private static Connection getConnection() throws IOException, SQLException {

        reader = new BufferedReader(new InputStreamReader(System.in));

//        System.out.println("Enter username:");
//        String username = reader.readLine();
//
//        System.out.println("Enter password:");
//        String password = reader.readLine();

        Properties properties = new Properties();
        properties.setProperty("user", "root");//username);
        properties.setProperty("password", "12345"); //password);
        return DriverManager.getConnection
                (URL + DATABASE, properties);
    }
}
