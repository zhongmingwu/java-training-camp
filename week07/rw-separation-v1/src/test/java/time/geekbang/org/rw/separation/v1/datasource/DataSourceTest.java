package time.geekbang.org.rw.separation.v1.datasource;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DataSourceTest {

    @Autowired
    private DataSource master;
    @Autowired
    @Qualifier("slave1")
    private DataSource slave1;
    @Autowired
    @Qualifier("slave2")
    private DataSource slave2;

    @Test
    public void databaseSourceTest() throws SQLException {
        assertNotNull(master.getConnection());
        assertNotNull(slave1.getConnection());
        assertNotNull(slave2.getConnection());

        String masterUrl = master.getConnection().getMetaData().getURL();
        String slave1Url = slave1.getConnection().getMetaData().getURL();
        String slave2Url = slave2.getConnection().getMetaData().getURL();

        assertNotEquals(masterUrl, slave1Url);
        assertNotEquals(slave1Url, slave2Url);
        assertNotEquals(slave2Url, masterUrl);

        log.info("masterUrl ==> {}", masterUrl);
        log.info("slave1Url ==> {}", slave1Url);
        log.info("slave2Url ==> {}", slave2Url);

        // output
        // 2020-12-03 02:09:10.508  INFO 19269 --- [           main] t.g.o.r.s.v1.datasource.DataSourceTest   : masterUrl ==> jdbc:mysql://localhost:13306/e_commerce?useSSL=false
        // 2020-12-03 02:09:10.509  INFO 19269 --- [           main] t.g.o.r.s.v1.datasource.DataSourceTest   : slave1Url ==> jdbc:mysql://localhost:23306/e_commerce?useSSL=false
        // 2020-12-03 02:09:10.509  INFO 19269 --- [           main] t.g.o.r.s.v1.datasource.DataSourceTest   : slave2Url ==> jdbc:mysql://localhost:33306/e_commerce?useSSL=false
    }
}
