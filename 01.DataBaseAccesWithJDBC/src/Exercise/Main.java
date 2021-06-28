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
            case "8" -> exerciseEight();
            case "9" -> exerciseNine();
        }
    }

    private static void exerciseNine() throws IOException, SQLException {
        System.out.println("Enter minion ID:");
        int id = Integer.parseInt(reader.readLine());

        CallableStatement callableStatement = connection.prepareCall
                ("CALL usp_get_older(?);");

        callableStatement.setInt(1, id);
        callableStatement.execute();

        preparedStatement = connection.prepareStatement
                ("SELECT name ,age FROM minions");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf
                    ("%s %d%n", resultSet.getString(1),
                            resultSet.getInt(2));
        }
    }

    private static void exerciseEight() throws IOException, SQLException {
        System.out.println("Enter minion ID`s (separated by space):");

        String[] idTokens = reader.readLine().split("\\s+");

        List<String> minionIds = new LinkedList<>();
        Collections.addAll(minionIds, idTokens);

        //Preventing with for cycle adding more or less id`s as input.

        for (String minionId : minionIds) {

            query = "UPDATE minions " +
                    "SET age = age + 1 , name = LOWER(name)" +
                    "WHERE id IN (?);";

            preparedStatement = prepFunction(query);
            preparedStatement.setString(1, minionId);

            preparedStatement.executeUpdate();
        }
        query = "SELECT name, age FROM minions ";

        Statement print = connection.createStatement();
        ResultSet resultSet = print.executeQuery(query);

        while (resultSet.next()) {
            System.out.printf("%s, %s%n",
                    resultSet.getString(1), resultSet.getInt(2));
        }
    }

    private static void exerciseSeven() throws SQLException {
        List<String> names = new LinkedList<>();

        query = "SELECT name FROM minions;";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

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

    private static void exerciseSix() throws IOException, SQLException {
        System.out.println("Enter villain id");
        int id = Integer.parseInt(reader.readLine());

        int rows = deletedVillainsAffected(id);
        String villainName = findVillainNameById(id);

        deleteVillain(id);
        if (rows == 0) {
            System.out.println("No such villain was found");
            return;
        }

        System.out.printf("%s was deleted%n", villainName);
        System.out.printf("%d minions released", rows);
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
        System.out.println("Enter minion input :");
        String[] minionInput = reader.readLine().split(": ");

        String[] minionTokens = minionInput[1].split("\\s+");
        String minionName = minionTokens[0];
        int minionAge = Integer.parseInt(minionTokens[1]);
        String minionTown = minionTokens[2];

        System.out.println("Enter villain input :");
        String[] villainInput = reader.readLine().split(": ");

        String[] villainTokens = villainInput[1].split("\\s+");
        String villainName = villainTokens[0];

        int id = findTownId(minionTown);

        if (id == 0) {
            addToTown(minionTown);
            System.out.printf("Town %s was added to the database.%n", minionTown);
        }

        String villainNameFind = findVillainName(villainName);

        if (villainNameFind == null) {
            addVillain(villainName);
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        villainNameFind = villainNameFindByName(villainName);
        id = findTownId(minionTown);
        addMinion(minionName, minionAge, id);
        int minionId = findMinionByName(minionName);
        int villainId = findVillainIdByName(villainNameFind);
        addMinionToVillain(minionId, villainId);

        System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villainName);
    }

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

    private static void addMinionToVillain(int minionId, int villainId) throws SQLException {
        preparedStatement = prepFunction("UPDATE minions_villains " +
                "JOIN minions m on m.id = minions_villains.minion_id " +
                "join villains v on v.id = minions_villains.villain_id " +
                "SET minion_id = ?,villain_id= ? " +
                "WHERE m.id = minions_villains.minion_id and v.id = minions_villains.villain_id;");
        preparedStatement.setInt(1, minionId);
        preparedStatement.setInt(2, villainId);

        preparedStatement.executeUpdate();
    }

    private static int findVillainIdByName(String villainName) throws SQLException {
        int result = 0;
        preparedStatement = prepFunction("SELECT id FROM villains WHERE name = ?");
        preparedStatement.setString(1, villainName);
        ResultSet resultSet = preparedStatement.executeQuery();


        if (resultSet.next()) {
            result = resultSet.getInt("id");
        }
        return result;

    }

    private static int findMinionByName(String minionName) throws SQLException {
        int result = 0;
        preparedStatement = prepFunction("SELECT id FROM minions WHERE name = ?");
        preparedStatement.setString(1, minionName);
        ResultSet resultSet = preparedStatement.executeQuery();


        while (resultSet.next()) {
            result = resultSet.getInt("id");
        }
        return result;
    }

    private static void addMinion(String minionName, int minionAge, int id) throws SQLException {
        preparedStatement = prepFunction("INSERT INTO minions (name, age, town_id) VALUES (?,?,?)");
        preparedStatement.setString(1, minionName);
        preparedStatement.setInt(2, minionAge);
        preparedStatement.setInt(3, id);

        preparedStatement.executeUpdate();
    }

    private static String villainNameFindByName(String villainName) throws SQLException {
        preparedStatement = prepFunction("SELECT name FROM villains WHERE name = ?");
        preparedStatement.setString(1, villainName);
        ResultSet resultSet = preparedStatement.executeQuery();

        String name = null;
        while (resultSet.next()) {
            name = resultSet.getString(1);
        }
        return name;
    }

    private static void addVillain(String villainName) throws SQLException {
        preparedStatement = prepFunction("INSERT INTO villains(name) VALUES (?)");
        preparedStatement.setString(1, villainName);
        preparedStatement.executeUpdate();
    }

    private static String findVillainName(String villainName) throws SQLException {
        String result = null;

        preparedStatement = prepFunction("SELECT id FROM villains WHERE name = ?");
        preparedStatement.setString(1, villainName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            result = resultSet.getString(1);
        }
        return result;
    }

    private static void addToTown(String minionTown) throws SQLException {
        preparedStatement = prepFunction("INSERT INTO towns(name) VALUES (?)");
        preparedStatement.setString(1, minionTown);
        preparedStatement.executeUpdate();
    }

    private static int findTownId(String minionTown) throws SQLException {
        int idResult = 0;

        preparedStatement = prepFunction("SELECT id FROM towns WHERE name = ?");
        preparedStatement.setString(1, minionTown);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            idResult = resultSet.getInt(1);
        }
        return idResult;
    }

    private static int deletedVillainsAffected(int id) throws SQLException {
        preparedStatement = connection.prepareStatement
                ("DELETE FROM minions_villains WHERE villain_id = ?");
        preparedStatement.setInt(1, id);

        return preparedStatement.executeUpdate();
    }

    private static String findVillainNameById(int id) throws SQLException {
        String name = "";
        preparedStatement = connection.prepareStatement
                ("SELECT name FROM villains WHERE id = ?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            name = resultSet.getString(1);
        }
        return name;
    }

    private static int deleteVillain(int id) throws SQLException {
        preparedStatement = connection.prepareStatement
                ("DELETE FROM villains WHERE id = ?");
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    private static PreparedStatement prepFunction(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    private static Connection getConnection() throws IOException, SQLException {

        reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter username:");
        String username = reader.readLine();

        System.out.println("Enter password:");
        String password = reader.readLine();

        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        return DriverManager.getConnection
                (URL + DATABASE, properties);
    }
}
