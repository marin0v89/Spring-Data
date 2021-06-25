package Exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Main {
    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DATABASE = "minions_db";
    public static BufferedReader reader;
    public static Connection connection;
    public static String query;
    public static PreparedStatement preparedStatement;

    public static void main(String[] args) throws SQLException, IOException {

        connection = getConnection();

        System.out.println("Enter exercise index (Starting from 2):");
        String intro = reader.readLine();

        switch (intro) {
            case "2" -> exerciseTwo();
            case "3" -> exerciseThree();
            case "4" -> exerciseFour();
            case "5" -> exerciseFive();
            case "6" -> exerciseSix();
            case "7" -> exerciseSeven();
        }
    }

    private static void exerciseSeven() throws SQLException {
        List<String> names = new LinkedList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM minions;");

        while (resultSet.next()) {
            names.add(resultSet.getString(1));
        }
        int start = 0;
        int end = names.size() - 1;
        for (int i = 0; i < names.size(); i++) {
            System.out.println(i % 2 == 0
                    ? names.get(start++)
                    : names.get(end--));
//            if (i % 2 == 0) {
//                System.out.println(names.get(start++));
//            } else {
//                System.out.println(names.get(end--));
//            }
        }
    }

    private static void exerciseSix() {
    }

    private static void exerciseFive() throws IOException, SQLException {
        System.out.println("Enter country");
        String country = reader.readLine();

        query = "UPDATE  towns " +
                "SET name = UPPER(name) " +
                "WHERE country = ?;";

        preparedStatement = prepFunction(query);
        preparedStatement.setString(1, country);
        preparedStatement.executeUpdate();


        query = "SELECT name FROM towns WHERE country = ?";
        PreparedStatement preparedStatementTowns = prepFunction(query);
        preparedStatementTowns.setString(1, country);
        ResultSet resultSetTowns = preparedStatementTowns.executeQuery();

        int rows = preparedStatement.executeUpdate();

        if (rows == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.%n", rows);
        List<String> towns = new LinkedList<>();
        while (resultSetTowns.next()) {
            towns.add(resultSetTowns.getString(1));
        }
        System.out.println(String.join(", ", towns));
    }

    private static void exerciseFour() throws IOException, SQLException {
//        System.out.println("Enter minion input:");
//        String[] minionTokens = reader.readLine().split("\\s+");
//        String minionName = minionTokens[0];
//        int minionAge = Integer.parseInt(minionTokens[1]);
//        String minionTowns = minionTokens[2];
//
//        System.out.println("Enter villain input:");
//        String villainName = reader.readLine();
//
//        if (!checkEntityByName(minionTowns)) {
//            insertIntoTowns(minionTowns);
//        }
    }
//
//    private static void insertIntoTowns(String minionTowns) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement
//                ("INSERT INTO towns (name, country) VALUE(?, ?)");
//        preparedStatement.setString(1, minionTowns);
//        preparedStatement.setString(2, "NULL");
//        preparedStatement.execute();
//    }
//
//    private static boolean checkEntityByName(String entity) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement
//                ("SELECT * FROM towns WHERE name = ?");
//
//        preparedStatement.setString(1, entity);
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        return resultSet.next();
//    }

    private static void exerciseThree() throws IOException, SQLException {
        Set<String> result = new LinkedHashSet<>();
        System.out.println("Enter villain ID:");
        String villainID = reader.readLine();

        query = "SELECT m.name, m.age FROM minions AS m " +
                "        JOIN minions_villains mv on m.id = mv.minion_id " +
                "        JOIN villains v on mv.villain_id = v.id " +
                "        WHERE v.id = ?;";

        preparedStatement = prepFunction(query);

        preparedStatement.setString(1, villainID);
        ResultSet resultSet = preparedStatement.executeQuery();

        query = "SELECT name FROM villains WHERE id = ?;";
        PreparedStatement villainName = prepFunction(query);
        villainName.setString(1, villainID);

        ResultSet nameSet = villainName.executeQuery();
        if (nameSet.next()) {
            System.out.println("Villain: " + nameSet.getString(1));
        } else {
            System.out.println("No villain with ID " + villainID + " exists in the database.");
        }
        int idCounter = 0;

        while (resultSet.next()) {

            result.add(String.format("%d %s %d",
                    ++idCounter, resultSet.getString(1),
                    resultSet.getInt(2)));
        }
        for (String s : result) {
            System.out.println(s);
        }
    }

    private static void exerciseTwo() throws SQLException {
        query = "SELECT v.name, COUNT(DISTINCT mv.minion_id) AS minion_count FROM villains AS v " +
                "JOIN minions_villains AS mv ON v.id = mv.villain_id " +
                "GROUP BY v.name " +
                "HAVING minion_count > 15;";
        preparedStatement = prepFunction(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n%n", resultSet.getString("name")
                    , resultSet.getInt(2));
        }
    }

    private static PreparedStatement prepFunction(String query) throws SQLException {
        return connection.prepareStatement(query);
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
