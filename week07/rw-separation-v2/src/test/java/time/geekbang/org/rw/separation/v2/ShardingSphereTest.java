package time.geekbang.org.rw.separation.v2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ShardingSphereTest {

    @Resource
    private DataSource dataSource;

    @Test
    public void test() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String readSql = "select * from `order` limit 3";
            String writeSql = "insert into `order` VALUES (null,0,0,0,0,0,0,0,0,0)";

            for (int i = 0; i < 3; i++) {
                statement.executeQuery(readSql);
            }

            log.info("============= executeUpdate begin =============");
            statement.executeUpdate(writeSql);
            log.info("============= executeUpdate end =============");

            for (int i = 0; i < 3; i++) {
                statement.executeQuery(readSql);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
