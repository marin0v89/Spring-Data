package Lab;

import java.sql.*;
import java.util.Scanner;

public class diabloDatabase {
    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);
        Connection connection = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/diablo", "root", "12345");

        String username = scan.nextLine();

        PreparedStatement stmt = connection.prepareStatement
                ("""
                        SELECT u.user_name, COUNT(g.name) AS count
                        FROM users AS u
                                 JOIN users_games AS ug on u.id = ug.user_id
                                 JOIN games g on ug.game_id = g.id
                        WHERE u.user_name  = ?
                        GROUP BY user_name;""");

        PreparedStatement uName = connection.prepareStatement
                ("SELECT *  FROM users WHERE user_name = ?");

        stmt.setString(1, username);
        uName.setString(1, username);

        ResultSet rs = stmt.executeQuery();
        ResultSet un = uName.executeQuery();

        if (!rs.isBeforeFirst()) {
            System.out.println("No such user exists");
        } else {
            while (rs.next() && un.next()) {
                System.out.println("User: " + username);
                System.out.println
                        (un.getString("first_name") + " " + un.getString("last_name")
                                + " has played " + rs.getString("count") + " games");
            }
            connection.close();
        }
    }
}
