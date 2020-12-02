package time.geekbang.org.rw.separation.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ShardingSphere1Test {

    @Resource
    private DataSource dataSource;

    @Test
    public void test() throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Statement st = con.createStatement();

            //从slave0读数据
            ResultSet rs = st.executeQuery("select * from `order` limit 3");
            while (rs.next()) {
                System.out.println(rs.toString());
            }

            //从slave0读数据
            rs = st.executeQuery("select * from `order` limit 3");
            while (rs.next()) {
                System.out.println(rs.toString());
            }

            //写入master
            st.executeUpdate("insert into `order` VALUES (null,0,0,0,0,0,0,0,0,0)");

            //从master读数据
            rs = st.executeQuery("select * from `order` limit 3");
            while (rs.next()) {
                System.out.println(rs.getString(1) + "|" + rs.getString(2));
            }

            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
}
