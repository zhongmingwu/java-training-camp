package time.geekbang.org;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcPreparedStatementAndTransaction {

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
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE Employees SET age = age +1 WHERE id = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                con.setAutoCommit(false);
                preparedStatement.setInt(1, 100);
                preparedStatement.executeUpdate();
                preparedStatement.setInt(1, 103);
                preparedStatement.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                try {
                    e.printStackTrace();
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
