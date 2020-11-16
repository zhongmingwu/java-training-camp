package time.geekbang.org;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @see http://zetcode.com/articles/hikaricp/
 */
public class JdbcHikariCp {

    // START TRANSACTION;
    // DROP TABLE IF EXISTS Cars;
    //
    // CREATE TABLE Cars(Id INTEGER PRIMARY KEY, Name VARCHAR(50), Price INTEGER);
    // INSERT INTO Cars VALUES(1, 'Audi', 52642);
    // INSERT INTO Cars VALUES(2, 'Mercedes', 57127);
    // INSERT INTO Cars VALUES(3, 'Skoda', 9000);
    // INSERT INTO Cars VALUES(4, 'Volvo', 29000);
    // INSERT INTO Cars VALUES(5, 'Bentley', 350000);
    // INSERT INTO Cars VALUES(6, 'Citroen', 21000);
    // INSERT INTO Cars VALUES(7, 'Hummer', 41400);
    // INSERT INTO Cars VALUES(8, 'Volkswagen', 21600);
    //
    // COMMIT;

    public static void main(String[] args) {
        String configFile = JdbcHikariCp.class.getClassLoader().getResource("db.properties").getPath();
        HikariDataSource ds = new HikariDataSource(new HikariConfig(configFile));

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = ds.getConnection();
            pst = con.prepareStatement("SELECT * FROM Cars");
            rs = pst.executeQuery();
            while (rs.next()) {
                System.out.format("%d %s %d %n", rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JdbcHikariCp.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
                ds.close();
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(JdbcHikariCp.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        // Output
        // [main] INFO com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Started.
        // 1 Audi 52642
        // 2 Mercedes 57127
        // 3 Skoda 9000
        // 4 Volvo 29000
        // 5 Bentley 350000
        // 6 Citroen 21000
        // 7 Hummer 41400
        // 8 Volkswagen 21600
        // [main] INFO com.zaxxer.hikari.pool.HikariPool - HikariPool-1 - Close initiated...
        // [main] INFO com.zaxxer.hikari.pool.HikariPool - HikariPool-1 - Closed.
    }
}
