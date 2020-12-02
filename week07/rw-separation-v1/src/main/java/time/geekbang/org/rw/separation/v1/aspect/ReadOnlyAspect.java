package time.geekbang.org.rw.separation.v1.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import time.geekbang.org.rw.separation.v1.datasource.DataSourceSelector;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class ReadOnlyAspect {

    private final DataSourceSelector dataSourceSelector;

    public ReadOnlyAspect(DataSourceSelector dataSourceSelector) {
        this.dataSourceSelector = dataSourceSelector;
    }

    @Pointcut("@annotation(time.geekbang.org.rw.separation.v1.annotation.ReadOnly)")
    public void readOnly() {
    }

    @Around("readOnly()")
    public List<Map<String, Object>> changeDataSource(ProceedingJoinPoint point) throws Throwable {
        Object[] argv = point.getArgs();
        Object oldDataSource = argv[0];
        DataSource newDataSource = dataSourceSelector.selectSlave();
        argv[0] = newDataSource;
        if (oldDataSource instanceof DataSource) {
            String oldUrl = ((DataSource) oldDataSource).getConnection().getMetaData().getURL();
            String newUrl = newDataSource.getConnection().getMetaData().getURL();
            log.info("change datasource : {} -> {}", oldUrl, newUrl);
        } else {
            log.info("change datasource : {} -> {}", oldDataSource, newDataSource);
        }
        return (List<Map<String, Object>>) point.proceed(argv);
    }
}
