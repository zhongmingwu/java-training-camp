package time.geekbang.org;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;

import static org.junit.Assert.assertEquals;

public class XaTransactionTest {

    private static final String SQL_TRUNCATE = "TRUNCATE t_order";
    private static final String SQL_INSERT = "INSERT INTO t_order (id, user_id) VALUES (?, ?)";
    private static final String SQL_COUNT = "SELECT COUNT(1) FROM t_order";
    private static final int N = 100;

    private final DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(new File(System.getProperty("dataSourceFile")));
    private Connection connection;

    public XaTransactionTest() throws IOException, SQLException {
    }

    @Before
    public void setUp() throws SQLException {
        connection = dataSource.getConnection();
        clearDate();
        TransactionTypeHolder.set(TransactionType.XA);
    }

    @After
    public void destroy() throws SQLException {
        connection.close();
        clearDate();
        TransactionTypeHolder.clear();
    }

    @Test
    public void xaTransactionTest() throws SQLException {
        for (int i = 0; i < 2; i++) {
            insert(N * (i + 1));
            assertEquals(N, count());
        }

        // output:
        //  insert success, n=100
        //  insert fail, try to rollback, n=200, cause=Duplicate entry '0' for key 't_order_0.PRIMARY'
    }

    private synchronized void clearDate() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(SQL_TRUNCATE);
        connection.commit();
    }

    private synchronized void insert(int n) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            connection.setAutoCommit(false);
            for (int i = 0; i < n; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setLong(2, i);
                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("insert success, n=" + n);
        } catch (SQLException e) {
            System.err.println("insert fail, try to rollback, n=" + n + ", cause=" + e.getMessage());
            connection.rollback();
        }
    }

    private synchronized int count() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_COUNT);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        throw new RuntimeException("count fail");
    }
}
