package time.geekbang.org;

import java.sql.*;

public class JdbcRaw {

    // CREATE TABLE Employees (id INT NOT NULL, age INT NOT NULL, first VARCHAR(255), last VARCHAR(255));
    // INSERT INTO Employees VALUES (100, 18, 'Zara', 'Ali');
    // INSERT INTO Employees VALUES (101, 25, 'Mahnaz', 'Fatma');
    // INSERT INTO Employees VALUES (102, 30, 'Zaid', 'Khan');
    // INSERT INTO Employees VALUES (103, 28, 'Sumit', 'Mittal');

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, first, last, age FROM Employees";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                String first = rs.getString("first");
                String last = rs.getString("last");
                System.out.print("ID: " + id);
                System.out.print(", Age: " + age);
                System.out.print(", First: " + first);
                System.out.println(", Last: " + last);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
