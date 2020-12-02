package time.geekbang.org.rw.separation.v1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import time.geekbang.org.rw.separation.v1.datasource.DataSourceSelector;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Autowired
    private DataSourceSelector dataSourceSelector;
    @Autowired
    private OrderService orderService;

    @Test
    public void insertAndQueryTest() {
        orderService.insert(dataSourceSelector.getMaster(), "insert into `order` VALUES (null,0,0,0,0,0,0,0,0,0)");

        for (int i = 0; i < 2; i++) {
            orderService.query(dataSourceSelector.getMaster(), "select * from `order` limit 3");
        }

        // output
        // 2020-12-03 02:11:39.642  INFO 19310 --- [           main] t.g.o.r.s.v.s.impl.OrderServiceImpl      : try to insert, dataSource=jdbc:mysql://localhost:13306/e_commerce?useSSL=false, sql=insert into `order` VALUES (null,0,0,0,0,0,0,0,0,0)
        // 2020-12-03 02:11:39.691  INFO 19310 --- [           main] t.g.o.r.s.v1.aspect.ReadOnlyAspect       : change datasource : jdbc:mysql://localhost:13306/e_commerce?useSSL=false -> jdbc:mysql://localhost:33306/e_commerce?useSSL=false
        // 2020-12-03 02:11:39.705  INFO 19310 --- [           main] t.g.o.r.s.v.s.impl.OrderServiceImpl      : try to query, dataSource=jdbc:mysql://localhost:33306/e_commerce?useSSL=false, sql=select * from `order` limit 3
        // 2020-12-03 02:11:39.747  INFO 19310 --- [           main] t.g.o.r.s.v1.aspect.ReadOnlyAspect       : change datasource : jdbc:mysql://localhost:13306/e_commerce?useSSL=false -> jdbc:mysql://localhost:23306/e_commerce?useSSL=false
        // 2020-12-03 02:11:39.763  INFO 19310 --- [           main] t.g.o.r.s.v.s.impl.OrderServiceImpl      : try to query, dataSource=jdbc:mysql://localhost:23306/e_commerce?useSSL=false, sql=select * from `order` limit 3
    }
}
