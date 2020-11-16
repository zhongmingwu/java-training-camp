package time.geekbang.org;

import java.sql.*;

public class JdbcStatement {

    // START TRANSACTION;
    // DROP TABLE IF EXISTS Employees;
    //
    // CREATE TABLE Employees (id INT NOT NULL, age INT NOT NULL, first VARCHAR(255), last VARCHAR(255));
    // INSERT INTO Employees VALUES (100, 18, 'Zara', 'Ali');
    // INSERT INTO Employees VALUES (101, 25, 'Mahnaz', 'Fatma');
    // INSERT INTO Employees VALUES (102, 30, 'Zaid', 'Khan');
    // INSERT INTO Employees VALUES (103, 28, 'Sumit', 'Mittal');
    //
    // COMMIT;

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
            String sql = "SELECT id, first, last, age FROM Employees";
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

        // Output
        // ID: 100, Age: 18, First: Zara, Last: Ali
        // ID: 101, Age: 25, First: Mahnaz, Last: Fatma
        // ID: 102, Age: 30, First: Zaid, Last: Khan
        // ID: 103, Age: 28, First: Sumit, Last: Mittal
    }
}
