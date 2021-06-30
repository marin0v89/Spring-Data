package Lab;

import java.sql.*;
import java.util.Scanner;

public class softUniDatabase {
    public static void main(String[] args) throws SQLException {

        Scanner scan = new Scanner(System.in);
        Connection connection = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/soft_uni", "root", "12345");

        String salary = scan.nextLine();

        PreparedStatement stmt = connection.prepareStatement
                ("SELECT first_name, last_name FROM employees WHERE salary > ?");
        stmt.setString(1, salary);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
        }
    }
}
