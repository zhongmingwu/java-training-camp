package time.geekbang.org.rw.separation.v1.datasource;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceSelector {

    @Getter
    private final DataSource master;
    private final DataSource slave1;
    private final DataSource slave2;

    public DataSourceSelector(@Qualifier("master") DataSource master,
                              @Qualifier("slave1") DataSource slave1,
                              @Qualifier("slave2") DataSource slave2) {
        this.master = master;
        this.slave1 = slave1;
        this.slave2 = slave2;
    }

    public DataSource selectSlave() {
        return System.currentTimeMillis() % 2 == 0 ? slave1 : slave2;
    }
}
